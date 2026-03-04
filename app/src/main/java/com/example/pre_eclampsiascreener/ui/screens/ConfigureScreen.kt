package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import com.example.pre_eclampsiascreener.ui.viewmodels.ConfigureViewModel

@Composable
fun ConfigureScreen(
    modifier: Modifier = Modifier,
    configureViewModel: ConfigureViewModel = viewModel()
) {
    val configureUiState by configureViewModel.UiState.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Thresholds"
        )
        Text("Blood Pressure (mmHg)")
        Row() {
            Column {
                Text("Minimum")
                OutlinedTextField(
                    value = configureViewModel.minBloodPressure(),
                    onValueChange = configureViewModel::setMinBloodPressure,
                    modifier = Modifier.width(120.dp),
                    placeholder = { Text("90/60") },
                    singleLine = true,

                    )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Maximum")
                OutlinedTextField(
                    value = configureViewModel.maxBloodPressure(),
                    onValueChange = configureViewModel::setMaxBloodPressure,
                    modifier = Modifier.width(120.dp),
                    placeholder = { Text("120/80") },
                    singleLine = true,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = {},
            ) {
                Text("View Data")
            }
            Button(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.FactCheck,
                    contentDescription = null
                )
                Text("Done")
            }
        }
    }
}

@Composable
fun MinMaxRow(
    modifier: Modifier = Modifier,
    minValueChange: (String) -> Unit,
    minValue: String,
    maxValueChange: (String) -> Unit,
    maxValue: String,
    maxKeyBoardAction: ImeAction
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            value = minValue,
            onValueChange = minValueChange,
            label = { Text("min") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )
        TextField(
            value = maxValue,
            onValueChange = maxValueChange,
            label = { Text("max") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = maxKeyBoardAction
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigureScreenPreview() {
    AppTheme() {
        ConfigureScreen(Modifier.fillMaxSize())
    }
}