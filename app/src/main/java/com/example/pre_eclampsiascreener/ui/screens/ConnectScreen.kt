package com.example.pre_eclampsiascreener.ui.screens

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
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
import com.example.pre_eclampsiascreener.ui.components.DeviceInfoCard
import com.example.pre_eclampsiascreener.ui.viewmodels.ScanViewModel
import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.core.Manager
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.ble.environment.android.compose.LocalEnvironmentOwner.current

@Composable
fun ConnectScreen(
    onDeviceSelect: (Peripheral) -> Unit,
    modifier: Modifier = Modifier,
    vm: ScanViewModel = viewModel(),
){
    val environment = current
    val state by vm.uiState.collectAsStateWithLifecycle()

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

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            vm.startScan()
        } else {
            vm.stopScan()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            vm.stopScan() // add a fun stopScan() { scanJob?.cancel() }
        }
    }

    when {
        state.bluetoothState != Manager.State.POWERED_ON -> {
            Button(onClick = {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }) {
                Text("Enable Bluetooth")
            }
        }

        !permissionGranted -> {
            Button(onClick = { launcher.launch(permissions) }) {
                Text("Grant required permissions")
            }
        }

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
                        DeviceInfoCard(
                            device.peripheral.name,
                            device.peripheral.address,
                            device.rssi,
                            onDeviceClick = {
                                vm.onPeripheralSelected(device.peripheral)
                                onDeviceSelect(device.peripheral)
                            }
                        )
                        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    }
                }
            }

        }
    }
}