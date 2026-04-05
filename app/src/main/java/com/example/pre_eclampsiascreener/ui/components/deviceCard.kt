package com.example.pre_eclampsiascreener.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.pre_eclampsiascreener.ble.ConnectState


@Preview(showBackground = true)
@Composable
fun DeviceInfoPreviewIdle() {
    DeviceInfoCard(null, "FD:6D:43:3B:F0:E8", -90, ConnectState.Idle,{})
}

@Preview(showBackground = true)
@Composable
fun DeviceInfoPreviewLoading() {
    DeviceInfoCard(null, "FD:6D:43:3B:F0:E8", -90, ConnectState.Connecting,{})
}

@Preview(showBackground = true)
@Composable
fun DeviceInfoPreviewError() {
    DeviceInfoCard(null, "FD:6D:43:3B:F0:E8", -90, ConnectState.Failed,{})
}

@Composable
fun DeviceInfoCard(
    name: String?,
    address: String,
    rssi: Int,
    connectState: ConnectState = ConnectState.Idle,
    onDeviceClick: () -> Unit
) {
    val isAbleToConnect = connectState == ConnectState.Idle || connectState == ConnectState.Failed

    TextButton(
        onClick = onDeviceClick,
        enabled = isAbleToConnect,
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
                    name ?: "Unknown",
                    fontSize = 30.sp,
                )
                Text(
                    address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

//            Log.d("ui", "$connectState")
            Box(modifier = Modifier, contentAlignment = Alignment.CenterEnd) {
                when (connectState) {
                    ConnectState.Connecting -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )

                    ConnectState.Failed -> Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Failed",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )

                    else -> {
                        Row {
                            Text(rssi.toString())
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.device_connect_button),
                                fontSize = 18.sp,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}