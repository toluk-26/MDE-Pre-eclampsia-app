package com.example.pre_eclampsiascreener.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pre_eclampsiascreener.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SmallTopAppBarExample() {
    Scaffold(
        topBar = {
            Banner(Modifier, {})
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            DeviceInfoBanner(
                Modifier,
                "PES-XXXX",
                "0000001",
                75
            )
            Text(
                text = "hi",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(modifier: Modifier, onBackClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors( // TODO: change
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = {
            Text(stringResource(R.string.app_name))
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewDeviceInfoBanner(){
    Row(Modifier.background(colorResource(R.color.white))) {
        DeviceInfoBanner(Modifier, "PES-XXXX", "0000001", 75)
    }
}


@Composable
fun DeviceInfoBanner(modifier: Modifier, devID: String, patientID: String, battery: Int){
    val (batteryIcon, batteryDescription) = when (battery) {
        in 0..4 -> R.drawable.outline_battery_android_0_24 to R.string.battery_0_description
        in 5..20 -> R.drawable.outline_battery_android_frame_1_24 to R.string.battery_1_description
        in 21..36 -> R.drawable.outline_battery_android_frame_2_24 to R.string.battery_2_description
        in 37..52 -> R.drawable.outline_battery_android_frame_3_24 to R.string.battery_3_description
        in 53..68 -> R.drawable.outline_battery_android_frame_4_24 to R.string.battery_4_description
        in 69..84 -> R.drawable.outline_battery_android_frame_5_24 to R.string.battery_5_description
        in 85..85 -> R.drawable.outline_battery_android_frame_6_24 to R.string.battery_6_description
        in 96..100 -> R.drawable.outline_battery_android_full_24 to R.string.battery_full_description
        else -> R.drawable.outline_battery_android_alert_24 to R.string.battery_alert_description
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.purple_500)), // TODO: change colors
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.device_id) + ": $devID",
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Start,
        )
        Text(
            text = stringResource(R.string.patient_id) + ": $patientID",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(batteryIcon),
                contentDescription = stringResource(batteryDescription),
            )
            Spacer(modifier= Modifier.width(4.dp))
            Text(
                text = "$battery%"
            )
        }

    }
}