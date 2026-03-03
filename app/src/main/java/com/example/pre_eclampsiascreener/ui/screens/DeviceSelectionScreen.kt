package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pre_eclampsiascreener.data.DeviceInfo
import com.example.pre_eclampsiascreener.ui.components.DeviceInfoCard

@Composable
fun DeviceSelectionScreen(
    devices: List<DeviceInfo>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(6.dp, 0.dp)
    ) {
        if(devices.isEmpty()) {
            //TODO: empty page
            Text(
                text = "no devices"
            )
        } else {
            devices.forEach { device ->
                DeviceInfoCard(device, {})
                Divider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }

}