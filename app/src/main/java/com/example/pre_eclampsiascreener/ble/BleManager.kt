package com.example.pre_eclampsiascreener.ble

import android.util.Log
import com.example.pre_eclampsiascreener.ble.repo.ConfigRepository
import com.example.pre_eclampsiascreener.ble.repo.DeviceInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.client.android.native
import no.nordicsemi.kotlin.ble.core.Phy
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi

private const val TAG = "BleManager"

class BleManager(environment: NativeAndroidEnvironment, appScope: CoroutineScope) {
    val centralManager: CentralManager =
        CentralManager.native(environment, appScope)

    private val _connectState = MutableStateFlow<ConnectState>(ConnectState.Idle)
    val connectState: StateFlow<ConnectState> = _connectState.asStateFlow()

    private var connectedPeripheral: Peripheral? = null
    private var connectScope: CoroutineScope? = null

    fun close() {
        connectScope?.cancel()
        connectScope = null
        centralManager.close()
    }

    fun connect(peripheral: Peripheral) {
        val scope = CoroutineScope(Dispatchers.IO).also { connectScope = it }
        connectScope = CoroutineScope(context = Dispatchers.IO)
            .apply {
                launch {
                    try {
                        _connectState.value = ConnectState.Connecting
                        Log.d(TAG, "connect state set to connecting")
                        // register profiles
                        registerServices(peripheral, scope)

                        // try connection
                        Log.d(TAG, "Connecting to ${peripheral.name}...")
                        centralManager.connect(
                            peripheral = peripheral,
                            CentralManager.ConnectionOptions.Direct(
                                timeout = 3.seconds,
                                retry = 2,
                                retryDelay = 1.seconds,
                                Phy.PHY_LE_2M,
                                automaticallyRequestHighestMtu = true
                            )
                        )
                        DeviceInfoRepository.setDeviceName(peripheral.name)
                        connectedPeripheral = peripheral
                        Log.d(TAG, "Connected to ${peripheral.name}!")

                        ConfigRepository.data.first{it.pid != null}
                        _connectState.value = ConnectState.Connected
                        Log.d(TAG, "connect state set to connected")
                    } catch (e: Exception) {
                        Log.e(TAG, "Connect failed, $e")
                        _connectState.value = ConnectState.Failed
                        Log.d(TAG, "connect state set to fail")
                        connectScope?.cancel()
                        connectScope = null
                    }
                }
            }
    }

    fun disconnect() {
        connectScope?.launch {
            try {
                connectedPeripheral?.disconnect()
                Log.d(TAG, "Disconnected from ${connectedPeripheral?.name}!")
            } catch (e: Exception) {
                Log.e(TAG, "Disconnect failed, $e")
            } finally {
                _connectState.value = ConnectState.Idle
                Log.d(TAG, "connect state set to disconnect")
                connectedPeripheral = null
                Profile.entries.forEach { it.clearRepository() }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun registerServices(
        peripheral: Peripheral,
        scope: CoroutineScope
    ) {
        Profile.entries.forEach { profile ->
            peripheral.profile(
                serviceUuid = profile.uuid,
                required = false
            ) { remoteService ->
                profile.createManager()
                    .let {
                        Log.d(TAG, "Service found, starting manager for $profile")
                        it.observeServiceInteractions(remoteService, scope)
                        awaitCancellation()
                    }

            }
        }
    }

    fun resetState() {
        _connectState.value = ConnectState.Idle
        Log.d(TAG, "connect state set to idle")
    }
}