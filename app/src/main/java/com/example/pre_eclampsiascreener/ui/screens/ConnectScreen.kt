package com.example.pre_eclampsiascreener.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import no.nordicsemi.kotlin.ble.core.android.AndroidEnvironment
import no.nordicsemi.kotlin.ble.environment.android.compose.LocalEnvironmentOwner
import no.nordicsemi.kotlin.ble.environment.android.compose.LocalEnvironmentOwner.current

@Composable
fun ConnectScreen(
    modifier: Modifier = Modifier,
    vm: ScanViewModel = viewModel(),
){
    val environment = LocalEnvironmentOwner.current
    val bleState by vm.bleState.collectAsStateWithLifecycle()
    val state by vm.uiState.collectAsStateWithLifecycle()

    // Scanning requires BLUETOOTH_SCAN permission, but
    // reading device name or bond state requires BLUETOOTH_CONNECT permission.
    val permissions = arrayOf(
        AndroidEnvironment.Permission.BLUETOOTH_SCAN,
        AndroidEnvironment.Permission.BLUETOOTH_CONNECT,
    )
    var permissionGranted by remember {
        mutableStateOf(environment.isBluetoothScanPermissionGranted && environment.isBluetoothConnectPermissionGranted)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            // This may not work.
            // permissionGranted = it.values.all { true }
            // Use this instead:
            permissionGranted = environment.isBluetoothScanPermissionGranted && environment.isBluetoothConnectPermissionGranted
        }
    )

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            vm.startScan()
        } else {
//            vm.stopScan()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            vm.stopScan() // add a fun stopScan() { scanJob?.cancel() }
        }
    }

    if (permissionGranted) {
        // Both Bluetooth and Location permissions are granted.
        // We can now start scanning.
        if(state.peripherals.isEmpty()) {
            //TODO: empty page
            Text(
                text = "no devices"
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(6.dp, 0.dp)
            ) {
                items(state.peripherals.values
                    .sortedByDescending { it.rssi }
                    .toList()
                ) { device ->
                    DeviceInfoCard(
                        device.peripheral.name,
                        device.peripheral.address,
                        device.rssi,
                        {})
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

    } else {
        Button(
            onClick = {  launcher.launch(permissions) }
        ) {
            Text("Grant required permissions")
        }
    }
}