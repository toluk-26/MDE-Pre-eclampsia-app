package com.example.pre_eclampsiascreener.ble.managers

import android.util.Log
import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.ble.ServiceManager
import com.example.pre_eclampsiascreener.ble.parsers.parseTransferData
import com.example.pre_eclampsiascreener.ble.parsers.toInt
import com.example.pre_eclampsiascreener.ble.repo.TransferRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.kotlin.ble.client.RemoteCharacteristic
import no.nordicsemi.kotlin.ble.client.RemoteService
import no.nordicsemi.kotlin.ble.core.WriteType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TransferManager : ServiceManager {
    override val profile: Profile = Profile.TRANSFER
    override suspend fun observeServiceInteractions(
        remoteService: RemoteService,
        scope: CoroutineScope
    ) {
        dataCharacteristic = remoteService.characteristics.firstOrNull {
            it.uuid == DATA_CHARACTERISTIC_UUID
        } ?: throw IllegalStateException("Data characteristic not found")

        try {
            dataCharacteristic.subscribe()
                .mapNotNull { it.parseTransferData() }
                .onEach { TransferRepository.add(it) }
                .onCompletion { TransferRepository.clear() }
                .catch { e ->
                    e.printStackTrace()
                    Log.e(TAG, e.toString())
                }
                .launchIn(scope)
        } catch (e: Exception){
            Log.e(TAG, "Error subscribing to transfer: ${e.toString()}")
        }

        try {
            remoteService.characteristics
                .firstOrNull { it.uuid == DATA_SIZE_CHARACTERISTIC_UUID}
                ?.subscribe()
                ?.mapNotNull { it.toInt() }
                ?.onEach { TransferRepository.checkSize(it) }
                ?.onCompletion { TransferRepository.clear() }
                ?.catch { e ->
                    e.printStackTrace()
                    Log.e(TAG, e.toString())
                }
                ?.launchIn(scope)
        } catch (e: Exception){
            Log.e(TAG, "Error subscribing to transfer: ${e.toString()}")
        }

        try{
            triggerCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == TRIGGER_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Data characteristic not found")
        } catch (e: Exception){
            Log.e(TAG, "Error finding Transfer Trigger Characteristic: ${e.toString()}")
        }
    }

    companion object {
        const val TAG = "TransferManager"

        private val TRIGGER_CHARACTERISTIC_UUID: Uuid =
            Uuid.parse("8d760001-7bdb-4430-a1b9-e7d26fb2b981")
        private val DATA_CHARACTERISTIC_UUID: Uuid =
            Uuid.parse("8d760002-7bdb-4430-a1b9-e7d26fb2b981")
        private val DATA_SIZE_CHARACTERISTIC_UUID: Uuid =
            Uuid.parse("8d760003-7bdb-4430-a1b9-e7d26fb2b981")

        private lateinit var triggerCharacteristic: RemoteCharacteristic
        private lateinit var dataCharacteristic: RemoteCharacteristic

        suspend fun triggerTransfer() {
            try {
                if (::triggerCharacteristic.isInitialized and ::dataCharacteristic.isInitialized) {
                    triggerCharacteristic.write(byteArrayOf(1), WriteType.WITHOUT_RESPONSE)
                }
            } catch (e: Exception) {
                Log.e(TimeManager.TAG, "Write error: ${e.message}")
            }
        }
    }
}