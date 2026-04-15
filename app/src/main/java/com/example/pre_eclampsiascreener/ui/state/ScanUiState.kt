package com.example.pre_eclampsiascreener.ui.state

import androidx.compose.runtime.mutableStateMapOf
import com.example.pre_eclampsiascreener.ble.ConnectState
import com.example.pre_eclampsiascreener.data.ScannedDevice
import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.core.Manager

data class ScanUiState(
    val bluetoothState: Manager.State = Manager.State.POWERED_OFF,
    val isScanning: Boolean = false,
//    val peripherals: Map<String, ScannedDevice> = mutableStateMapOf(),
    val selectedPeripheral: Peripheral? = null,
    val connectState: ConnectState = ConnectState.Idle,
    val error: String? = null,
)