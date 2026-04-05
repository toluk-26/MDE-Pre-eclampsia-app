package com.example.pre_eclampsiascreener.ble.managers

import android.util.Log
import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.ble.ServiceManager
import com.example.pre_eclampsiascreener.ble.ServiceManager.Companion.TAG_DATA
import com.example.pre_eclampsiascreener.ble.parsers.toBoolean
import com.example.pre_eclampsiascreener.ble.parsers.toByteArray
import com.example.pre_eclampsiascreener.ble.parsers.toFloatList
import com.example.pre_eclampsiascreener.ble.parsers.toMinMaxList
import com.example.pre_eclampsiascreener.ble.parsers.toUInt
import com.example.pre_eclampsiascreener.ble.repo.ConfigRepository
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
class ConfigManager : ServiceManager {
    override val profile: Profile = Profile.CONFIG

    override suspend fun observeServiceInteractions(
        remoteService: RemoteService,
        scope: CoroutineScope
    ) {
        try {
            remoteService.characteristics.firstOrNull {
                it.uuid == DEMO_MODE_CHARACTERISTIC_UUID
            }
                ?.read()?.toBoolean()
                ?.also {
                    Log.d(TAG_DATA, "demo mode = $it")
                    ConfigRepository.updateDemoMode(it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            pidCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == PID_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
            pidCharacteristic
                .read().toUInt()
                .also {
                    Log.d(TAG_DATA, "PID = $it")
                    ConfigRepository.updatePID(it)
                }

            pidCharacteristic
                .subscribe()
                .mapNotNull { it.toUInt() }
                .onEach {
                    Log.d(TAG_DATA, "PID: $it")
                    ConfigRepository.updatePID(it)
                }
                .onCompletion { ConfigRepository.clear() }
                .catch { e ->
                    Log.e(TAG, e.toString())
                }
                .launchIn(scope)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            diastolicCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == DIASTOLIC_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
            diastolicCharacteristic
                .subscribe()
                .mapNotNull { it.toMinMaxList() }
                .onEach {
                    Log.d(TAG_DATA, "Diastolic Min = ${it[0]}")
                    Log.d(TAG_DATA, "Diastolic Max = ${it[1]}")
                    ConfigRepository.updateDiastolic(it)
                }
                .onCompletion { ConfigRepository.clear() }
                .catch { e ->
                    Log.e(BatteryManager.TAG, e.toString())
                }
                .launchIn(scope)

            diastolicCharacteristic
                .read().toMinMaxList()
                .also {
                    Log.d(TAG_DATA, "Diastolic Min = ${it[0]}")
                    Log.d(TAG_DATA, "Diastolic Max = ${it[1]}")
                    ConfigRepository.updateDiastolic(it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            systolicCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == SYSTOLIC_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
            systolicCharacteristic
                .subscribe()
                .mapNotNull { it.toMinMaxList() }
                .onEach {
                    Log.d(TAG_DATA, "Systolic Min = ${it[0]}")
                    Log.d(TAG_DATA, "Systolic Max = ${it[1]}")
                    ConfigRepository.updateSystolic(it)
                }
                .onCompletion { ConfigRepository.clear() }
                .catch { e ->
                    Log.e(TAG, e.toString())
                }
                .launchIn(scope)

            systolicCharacteristic
                .read().toMinMaxList()
                .also {
                    Log.d(TAG_DATA, "Systolic Min = ${it[0]}")
                    Log.d(TAG_DATA, "Systolic Max = ${it[1]}")
                    ConfigRepository.updateSystolic(it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            diastolicCoefficientsCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == DIASTOLIC_COEFFICIENTS_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
            diastolicCoefficientsCharacteristic
                .subscribe()
                .mapNotNull { it.toFloatList() }
                .onEach {
                    Log.d(TAG_DATA, "Diastolic Coefficient M = ${it[0]}")
                    Log.d(TAG_DATA, "Diastolic Coefficient B = ${it[1]}")
                    ConfigRepository.updateDiastolicCoefficients(it)
                }
                .onCompletion { ConfigRepository.clear() }
                .catch { e ->
                    Log.e(TAG, e.toString())
                }
                .launchIn(scope)

            diastolicCoefficientsCharacteristic
                .read().toFloatList()
                .also {
                    Log.d(TAG_DATA, "Diastolic Coefficient M = ${it[0]}")
                    Log.d(TAG_DATA, "Diastolic Coefficient B = ${it[1]}")
                    ConfigRepository.updateDiastolicCoefficients(it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            systolicCoefficientsCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == SYSTOLIC_COEFFICIENTS_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
            systolicCoefficientsCharacteristic
                .subscribe()
                .mapNotNull { it.toFloatList() }
                .onEach {
                    Log.d(TAG_DATA, "Systolic Coefficient M = ${it[0]}")
                    Log.d(TAG_DATA, "Systolic Coefficient B = ${it[1]}")
                    ConfigRepository.updateSystolicCoefficients(it)
                }
                .onCompletion { ConfigRepository.clear() }
                .catch { e ->
                    Log.e(TAG, e.toString())
                }
                .launchIn(scope)

            systolicCoefficientsCharacteristic
                .read().toFloatList()
                .also {
                    Log.d(TAG_DATA, "Systolic Coefficient M = ${it[0]}")
                    Log.d(TAG_DATA, "Systolic Coefficient B = ${it[1]}")
                    ConfigRepository.updateSystolicCoefficients(it)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }

        try {
            newPatientCharacteristic = remoteService.characteristics.firstOrNull {
                it.uuid == NEW_PATIENT_CHARACTERISTIC_UUID
            } ?: throw IllegalStateException("Characteristic not found")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to init: ${e.toString()}")
        }
    }


    companion object {
        private const val TAG = "ConfigManager"

        private val DEMO_MODE_CHARACTERISTIC_UUID =
            Uuid.parse("32610001-7bdb-4430-a1b9-e7d26fb2b981")
        private val NEW_PATIENT_CHARACTERISTIC_UUID =
            Uuid.parse("32610002-7bdb-4430-a1b9-e7d26fb2b981")
        private val PID_CHARACTERISTIC_UUID = Uuid.parse("32610003-7bdb-4430-a1b9-e7d26fb2b981")
        private val DIASTOLIC_CHARACTERISTIC_UUID =
            Uuid.parse("32610004-7bdb-4430-a1b9-e7d26fb2b981")
        private val SYSTOLIC_CHARACTERISTIC_UUID =
            Uuid.parse("32610005-7bdb-4430-a1b9-e7d26fb2b981")
        private val DIASTOLIC_COEFFICIENTS_CHARACTERISTIC_UUID =
            Uuid.parse("32610006-7bdb-4430-a1b9-e7d26fb2b981")
        private val SYSTOLIC_COEFFICIENTS_CHARACTERISTIC_UUID =
            Uuid.parse("32610007-7bdb-4430-a1b9-e7d26fb2b981")

        private lateinit var newPatientCharacteristic: RemoteCharacteristic
        private lateinit var pidCharacteristic: RemoteCharacteristic
        private lateinit var diastolicCharacteristic: RemoteCharacteristic
        private lateinit var systolicCharacteristic: RemoteCharacteristic
        private lateinit var diastolicCoefficientsCharacteristic: RemoteCharacteristic
        private lateinit var systolicCoefficientsCharacteristic: RemoteCharacteristic

        suspend fun writeNewPatient() {
            try {
                if (::newPatientCharacteristic.isInitialized)
                    newPatientCharacteristic.write(byteArrayOf(1.toByte()), WriteType.WITH_RESPONSE)
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }

        suspend fun writePID(pid: Int) {
            try {
                if (::pidCharacteristic.isInitialized)
                    pidCharacteristic.write(pid.toByteArray(), WriteType.WITH_RESPONSE)
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }

        suspend fun writeDiastolic(values: List<Int>) {
            try {
                if (::diastolicCharacteristic.isInitialized)
                    diastolicCharacteristic.write(
                        byteArrayOf(
                            values[0].toByte(),
                            values[1].toByte()
                        ), WriteType.WITH_RESPONSE
                    )
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }

        suspend fun writeDiastolicCoefficients(values: List<Float>) {
            try {
                if (::diastolicCoefficientsCharacteristic.isInitialized)
                    diastolicCoefficientsCharacteristic.write(
                        values[0].toByteArray() + values[1].toByteArray(),
                        WriteType.WITH_RESPONSE
                    )
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }

        suspend fun writeSystolic(values: List<Int>) {
            try {
                if (::systolicCharacteristic.isInitialized)
                    systolicCharacteristic.write(
                        byteArrayOf(
                            values[0].toByte(),
                            values[1].toByte()
                        ), WriteType.WITH_RESPONSE
                    )
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }

        suspend fun writeSystolicCoefficients(values: List<Float>) {
            try {
                if (::systolicCoefficientsCharacteristic.isInitialized)
                    systolicCoefficientsCharacteristic.write(
                        values[0].toByteArray() + values[1].toByteArray(),
                        WriteType.WITH_RESPONSE
                    )
            } catch (e: Exception) {
                Log.e(TAG, "failed to write ${e.toString()}")
            }
        }
    }
}