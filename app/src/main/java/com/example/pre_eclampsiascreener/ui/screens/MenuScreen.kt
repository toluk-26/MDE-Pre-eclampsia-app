package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pre_eclampsiascreener.AppScreen
import com.example.pre_eclampsiascreener.ble.repo.TransferRepository
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    demoMode: Boolean = false
) {
    val scope = rememberCoroutineScope() // TODO: remove

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // View Data button
        MenuButton(
            { navController.navigate(AppScreen.ViewData.name) },
            AppScreen.ViewData.toString()
        )
        MenuButton(
            { navController.navigate(AppScreen.Configure.name) },
            AppScreen.Configure.toString()
        )
        MenuButton(
            { navController.navigate(AppScreen.NewPatient.name) },
            AppScreen.NewPatient.toString()
        )
        if (demoMode) {
            MenuButton(
                { navController.navigate(AppScreen.Console.name) },
                AppScreen.Console.toString()
            )
            MenuButton(
                { navController.navigate(AppScreen.Demo.name) },
                AppScreen.Demo.toString()
            )
        }
        TextButton(onClick = { scope.launch { TransferRepository.trigger() }}) {
            Text("trigger transfer")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreen(
            modifier = Modifier.fillMaxSize(),
            navController = rememberNavController(),
            demoMode = true
        )
    }
}

@Composable
fun MenuButton(
    onButtonClicked: () -> Unit = {},
    text: String
) {
    Button(
        onClick = onButtonClicked
    ) {
        Text(text)
    }
}