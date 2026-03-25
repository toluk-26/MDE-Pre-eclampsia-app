package com.example.pre_eclampsiascreener.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pre_eclampsiascreener.MainApplication
import com.example.pre_eclampsiascreener.ble.BleManager
import com.example.pre_eclampsiascreener.data.ScannedDevice
import com.example.pre_eclampsiascreener.ui.state.ScanUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.Peripheral
import kotlin.time.Duration

private const val TAG = "ScanViewModel"

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    private val bleManager: BleManager = (application as MainApplication).bleManager

    val centralManager: CentralManager = bleManager.centralManager

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null

    init {
        centralManager.state
            .onEach { state ->
                _uiState.update { it.copy(bluetoothState = state) }
            }
            .launchIn(viewModelScope)
    }

    fun startScan() {
        if (_uiState.value.isScanning) {
            Log.d(TAG, "not scanning. exit")
            return
        }

        // can scan
        scanJob = centralManager
            .scan(Duration.INFINITE) {
                Any {
                    Name(Regex("PES.*"))
                }
            }
            .onStart {
                Log.d(TAG, "Scan started")
                _uiState.update { currentState ->
                    currentState.copy(
                        isScanning = true,
                        error = null
                    )
                }
            }
//            .distinctByPeripheral() // if enable, change peripheral to list, not map
//            .map { it.peripheral }
//            .filterNot { _uiState.value.peripherals.containsKey(it.peripheral) }
            .onEach { scanResult ->
                val newPeripheral = scanResult.peripheral

                _uiState.update { current ->
                    current.copy(
                        peripherals = current.peripherals
                            .toMutableMap()
                            .apply {
                                this[newPeripheral] =
                                    this[newPeripheral]?.copy(
                                        rssi = scanResult.rssi
                                    )
                                        ?: ScannedDevice(
                                            newPeripheral,
                                            scanResult.rssi
                                        )
                            }
                    )
                }
            }
            .catch { e ->
                Log.e(TAG, "Scan error: $e")
                _uiState.update { currentState ->
                    currentState.copy(
                        error = e.message
                    )
                }
            }
            .onCompletion {
                Log.d(TAG, "Scan Completed")
                _uiState.update { currentState ->
                    currentState.copy(
                        isScanning = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun stopScan() {
        Log.d(TAG, "Stop Scanning. should be leaving page")
        scanJob?.cancel()
        _uiState.update { it.copy(isScanning = false, peripherals = emptyMap()) }
    }

    override fun onCleared() {
        super.onCleared()
        centralManager.close()
    }

    fun onPeripheralSelected(peripheral: Peripheral) {
        bleManager.connect(peripheral)
    }

    fun close() {
        bleManager.disconnect()
    }
}