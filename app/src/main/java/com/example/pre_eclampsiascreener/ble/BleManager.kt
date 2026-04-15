package com.example.pre_eclampsiascreener.ble

import android.util.Log
import com.example.pre_eclampsiascreener.ble.repo.ConfigRepository
import com.example.pre_eclampsiascreener.ble.repo.DeviceInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

//class BleManager(environment: NativeAndroidEnvironment, private val appScope: CoroutineScope) {
//    val centralManager: CentralManager =
//        CentralManager.native(environment, appScope)
//
//    private val _connectState = MutableStateFlow<ConnectState>(ConnectState.Idle)
//    val connectState: StateFlow<ConnectState> = _connectState.asStateFlow()
//
//    private var connectedPeripheral: Peripheral? = null
//    private var connectScope: CoroutineScope? = null
//
//    fun close() {
//        centralManager.close()
//    }
//
//    // BleManager.kt
//    private var connectJob: Job? = null
//
//    fun connect(peripheral: Peripheral) {
//        // Cancel any ongoing connection attempt before starting a new one
//        connectJob?.cancel()
//
//        connectJob = appScope.launch { // Use the appScope passed in the constructor
//            try {
//                _connectState.value = ConnectState.Connecting
//
//                // Note: registerServices must be launched in a way that can be cancelled
//                registerServices(peripheral, this)
//
//                centralManager.connect(
//                    peripheral = peripheral,
//                    options = CentralManager.ConnectionOptions.Direct(
//                        automaticallyRequestHighestMtu = true
//                    )
//                )
//
//                DeviceInfoRepository.setDeviceName(peripheral.name)
//                connectedPeripheral = peripheral
//
//                // Wait for specific data before declaring "Connected"
//                ConfigRepository.data.first { it.pid != null }
//                _connectState.value = ConnectState.Connected
//            } catch (e: Exception) {
//                Log.e(TAG, "Connect failed", e)
//                _connectState.value = ConnectState.Failed
//            }
//        }
//    }
//
//    fun disconnect() {
//        connectJob?.cancel() // This stops the connection logic and service observers
//        appScope.launch {
//            try {
//                connectedPeripheral?.disconnect()
//            } finally {
//                _connectState.value = ConnectState.Idle
//                connectedPeripheral = null
//                Profile.entries.forEach { it.clearRepository() }
//            }
//        }
//    }
//
//    @OptIn(ExperimentalUuidApi::class)
//    private suspend fun registerServices(
//        peripheral: Peripheral,
//        scope: CoroutineScope
//    ) {
//        Profile.entries.forEach { profile ->
//            peripheral.profile(
//                serviceUuid = profile.uuid,
//                required = false
//            ) { remoteService ->
//                profile.createManager()
//                    .let {
//                        Log.d(TAG, "Service found, starting manager for $profile")
//                        it.observeServiceInteractions(remoteService, scope)
//                        awaitCancellation()
//                    }
//
//            }
//        }
//    }
//
//    fun resetState() {
//        _connectState.value = ConnectState.Idle
//        Log.d(TAG, "connect state set to idle")
//    }
//}