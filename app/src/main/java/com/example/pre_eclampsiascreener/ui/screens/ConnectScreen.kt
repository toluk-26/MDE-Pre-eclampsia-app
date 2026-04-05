package com.example.pre_eclampsiascreener.ui.screens

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre_eclampsiascreener.ble.ConnectState
import com.example.pre_eclampsiascreener.ui.components.DeviceInfoCard
import com.example.pre_eclampsiascreener.ui.viewmodels.ScanViewModel
import kotlinx.coroutines.delay
import no.nordicsemi.kotlin.ble.core.Manager
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.ble.environment.android.compose.LocalEnvironmentOwner.current
import androidx.compose.runtime.collectAsState

@Composable
fun ConnectScreen(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    vm: ScanViewModel = viewModel(),
){
    val environment = current
    val state by vm.uiState.collectAsStateWithLifecycle()
    val connectState by vm.connectState.collectAsStateWithLifecycle()

    // permissions
    // Scanning requires BLUETOOTH_SCAN permission, but
    // reading device name or bond state requires BLUETOOTH_CONNECT permission.
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            AndroidEnvironment.Permission.BLUETOOTH_SCAN,
            AndroidEnvironment.Permission.BLUETOOTH_CONNECT,
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        )
    }

    var permissionGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                environment.isBluetoothScanPermissionGranted &&
                        environment.isBluetoothConnectPermissionGranted
            } else {
                environment.isLocationPermissionGranted
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            permissionGranted =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    environment.isBluetoothScanPermissionGranted &&
                            environment.isBluetoothConnectPermissionGranted
                } else {
                    environment.isLocationPermissionGranted
                }
        }
    )
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){}

    // on screen start
    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            vm.close()
            vm.startScan()
        } else {
            vm.stopScan()
        }
    }

    // on screen close
    DisposableEffect(Unit) {
        onDispose {
            vm.stopScan()
            vm.resetConnectionState()
        }
    }

    // on connectState change
    LaunchedEffect(connectState) {
        when (connectState) {
            is ConnectState.Connected -> {
                onSuccess()
            }
            is ConnectState.Failed -> {
                delay(2000)
                vm.resetConnectionState()
            }
            else -> {}
        }
    }

    when {
        // bluetooth is off
        state.bluetoothState != Manager.State.POWERED_ON -> {
            Button(onClick = {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }) {
                Text("Enable Bluetooth")
            }
        }

        // bluetooth needs permissions
        !permissionGranted -> {
            Button(onClick = { launcher.launch(permissions) }) {
                Text("Grant required permissions")
            }
        }

        // regular business
        else -> {
            // Both Bluetooth and Location permissions are granted.
            // We can now start scanning.
            if (state.peripherals.isEmpty()) {
                //TODO: empty page
                Text(
                    text = "no devices"
                )
            } else {
                LazyColumn(
                    modifier = modifier
                        .padding(6.dp, 0.dp)
                ) {
                    items(
                        state.peripherals.values
                        .sortedByDescending { it.rssi }
                        .toList()
                    ) { device ->
                        val isThisDeviceSelected = vm.uiState.collectAsState().value.selectedPeripheral == device.peripheral
                        val cs = if (isThisDeviceSelected) connectState else ConnectState.Idle
                        DeviceInfoCard(
                            device.peripheral.name,
                            device.peripheral.address,
                            device.rssi,
                            cs
                        ) {
                            vm.onPeripheralSelected(device.peripheral)
                        }
                        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    }
                }
            }

        }
    }
}