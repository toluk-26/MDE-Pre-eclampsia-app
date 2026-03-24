package com.example.pre_eclampsiascreener.ble.managers

import android.util.Log
import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.ble.ServiceManager
import com.example.pre_eclampsiascreener.ble.parser.BatteryLevelParser
import com.example.pre_eclampsiascreener.ble.repo.BatteryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.kotlin.ble.client.RemoteService
import no.nordicsemi.kotlin.ble.core.CharacteristicProperty
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

private const val TAG = "BatterManager"

private val BATTERY_LEVEL_CHARACTERISTIC_UUID: UUID =
    UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb")

class BatteryManager : ServiceManager {
    override val profile: Profile = Profile.BATTERY

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun observeServiceInteractions(
//        peripheral: Peripheral,
        remoteService: RemoteService,
        scope: CoroutineScope
    ) {
        val batteryChar = remoteService.characteristics
            .firstOrNull { it.uuid == BATTERY_LEVEL_CHARACTERISTIC_UUID.toKotlinUuid() }

        batteryChar?.let { characteristic ->
            // If the characteristic supports READ, read the initial value
            if (characteristic.properties.contains(CharacteristicProperty.READ)) {
                try {
                    characteristic.read()
                        .let {
                            BatteryLevelParser.parse(it)
                        }
                        ?.let { batteryLevel ->
                            BatteryRepository.updateBatteryLevel( batteryLevel)
                        }

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
                    .mapNotNull { BatteryLevelParser.parse(it) }
                    .onEach { batteryLevel ->
                        BatteryRepository.updateBatteryLevel( batteryLevel)
                    }
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