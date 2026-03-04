package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre_eclampsiascreener.ui.components.DeviceInfoCard
import com.example.pre_eclampsiascreener.ui.viewmodels.DeviceViewModel

@Composable
fun DeviceSelectionScreen(
    modifier: Modifier = Modifier,
    devicesVM: DeviceViewModel = viewModel(),
){
    Column(
        modifier = modifier
            .padding(6.dp, 0.dp)
    ) {
        if(devicesVM.listOfDevices().isEmpty()) {
            //TODO: empty page
            Text(
                text = "no devices"
            )
        } else {
            devicesVM.listOfDevices().forEach { device ->
                DeviceInfoCard(device, {})
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }

}