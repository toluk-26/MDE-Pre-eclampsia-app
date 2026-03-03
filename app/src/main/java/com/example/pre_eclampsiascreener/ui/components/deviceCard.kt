package com.example.pre_eclampsiascreener.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pre_eclampsiascreener.R
import com.example.pre_eclampsiascreener.data.DeviceInfo


@Preview(showBackground = true)
@Composable
fun DeviceInfoPreview() {
    val device = DeviceInfo("PES-F0E8", "FD:6D:43:3B:F0:E8", -43)
    DeviceInfoCard(device, {})
}

@Composable
fun DeviceInfoCard(device: DeviceInfo, onDeviceClick: () -> Unit) {
    TextButton(
        onClick = onDeviceClick,
        modifier = Modifier
//            .background(colorResource(R.color.grey))
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    device.name,
                    fontSize = 30.sp,
                )
                Text(
                    device.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = stringResource(R.string.device_connect_button),
                fontSize = 18.sp,
                textAlign = TextAlign.End,
            )

        }
    }
}