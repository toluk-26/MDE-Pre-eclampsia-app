package com.example.pre_eclampsiascreener.ble.managers

import android.util.Log
import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.ble.ServiceManager
import com.example.pre_eclampsiascreener.ble.repo.BatteryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.kotlin.ble.client.RemoteService
import no.nordicsemi.kotlin.ble.core.CharacteristicProperty
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExperimentalUuidApi
class BatteryManager : ServiceManager {
    companion object {
        const val TAG = "BatterManager"
        private val BATTERY_LEVEL_CHARACTERISTIC_UUID: Uuid =
            Uuid.parse("00002A19-0000-1000-8000-00805f9b34fb")
    }

    override val profile: Profile = Profile.BATTERY

    override suspend fun observeServiceInteractions(
//        peripheral: Peripheral,
        remoteService: RemoteService,
        scope: CoroutineScope
    ) {
        val batteryChar = remoteService.characteristics
            .firstOrNull { it.uuid == BATTERY_LEVEL_CHARACTERISTIC_UUID }

        batteryChar?.let { characteristic ->
            // If the characteristic supports READ, read the initial value
            if (characteristic.properties.contains(CharacteristicProperty.READ)) {
                try {
                    characteristic.read()[0].toInt()
                        .let { BatteryRepository.updateBatteryLevel(it) }
                } catch (e: Exception) {
                    Log.e(TAG, "Error reading battery level: ${e.message}")
                }
            }
            // Check if the characteristic supports NOTIFY or INDICATE property
            if (characteristic.properties.contains(CharacteristicProperty.NOTIFY)
                || characteristic.properties.contains(CharacteristicProperty.INDICATE)
            ) {
                // Start subscription for battery level updates
                characteristic.subscribe()
                    .mapNotNull { it[0].toInt() }
                    .onEach {  BatteryRepository.updateBatteryLevel(it) }
                    .onCompletion {
                        BatteryRepository.clear()
                    }
                    .catch { e ->
                        Log.e(TAG, e.toString())
                    }
                    .launchIn(scope)
            }
        }
    }
}