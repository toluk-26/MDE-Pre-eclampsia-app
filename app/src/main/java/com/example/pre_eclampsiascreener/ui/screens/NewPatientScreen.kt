package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pre_eclampsiascreener.ble.repo.TimeRepository
import com.example.pre_eclampsiascreener.ui.viewmodels.NewPatientViewModel

@Composable
fun NewPatientScreen(
    modifier: Modifier = Modifier,
    patientUiState: NewPatientViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        TimeRepository.writeTimezone(-3)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { },
                icon = { Icon(Icons.AutoMirrored.Outlined.ArrowForward, "Go to the next page") },
                text = { Text(text = "Next") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please input the patients ID"
            )
            TextField(
                value = patientUiState.idValue(),
                onValueChange = patientUiState::setPatientId,
                label = { Text("Patient ID #")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NewPatientScreenPreview() {
    val patientUiState: NewPatientViewModel = viewModel()
    patientUiState.setPatientId("P-XXXXXX")

    AppTheme {
        NewPatientScreen(
            Modifier.fillMaxSize(),
            patientUiState
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewPatientBlankPreview() {
    AppTheme {
        NewPatientScreen(
            Modifier.fillMaxSize(),
        )
    }
}