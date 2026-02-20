package com.example.pre_eclampsiascreener.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pre_eclampsiascreener.R

@Preview
@Composable
fun deviceInfoPreview() {
    deviceInfo("PES-0000", "FD:6D:43:3B:F0:E8")
}

@Composable
fun deviceInfo(deviceName: String, macAddr: String) {
    TextButton(
        onClick = {},
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
                    deviceName,
                    fontSize = 30.sp,
                )
                Text(
                    macAddr,
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