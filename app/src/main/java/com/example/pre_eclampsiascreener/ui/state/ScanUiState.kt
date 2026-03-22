package com.example.pre_eclampsiascreener.ui.state

import com.example.pre_eclampsiascreener.data.ScannedDevice
import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.core.Manager

data class ScanUiState(
    val bluetoothState: Manager.State = Manager.State.POWERED_OFF,
    val isScanning: Boolean = false,
    val peripherals: Map<Peripheral, ScannedDevice> = emptyMap(),
    val error: String? = null,
)