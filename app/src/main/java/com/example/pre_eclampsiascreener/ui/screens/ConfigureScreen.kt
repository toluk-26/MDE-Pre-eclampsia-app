package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre_eclampsiascreener.ui.components.ThresholdCard
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import com.example.pre_eclampsiascreener.ui.viewmodels.ConfigureViewModel

@Composable
fun ConfigureScreen(
    modifier: Modifier = Modifier,
    showViewDataButton: Boolean = true,
    configureViewModel: ConfigureViewModel = viewModel()
) {
    val uiState by configureViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Title
        Text(
            text = "Configuration",
            style = MaterialTheme.typography.headlineMedium
        )
        ThresholdCard(
            minSystolic = uiState.minSystolic,
            minDiastolic = uiState.minDiastolic,
            maxSystolic = uiState.maxSystolic,
            maxDiastolic = uiState.maxDiastolic,
            onMinChange = configureViewModel::setMinBloodPressure,
            onMaxChange = configureViewModel::setMaxBloodPressure
        )

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if(showViewDataButton) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Data")
                }
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.FactCheck,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigureScreenPreview() {
    AppTheme {
        ConfigureScreen(Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun NewPatientConfigureScreenPreview() {
    AppTheme {
        ConfigureScreen(
            Modifier.fillMaxSize(),
            false
        )
    }
}