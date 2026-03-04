package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pre_eclampsiascreener.AppScreen
import com.example.pre_eclampsiascreener.ui.theme.AppTheme

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    demoMode: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // View Data button
        MenuButton(
            { navController.navigate(AppScreen.ViewData) },
            AppScreen.ViewData.toString()
        )
        MenuButton(
            { navController.navigate(AppScreen.Configure) },
            AppScreen.Configure.toString()
        )
        MenuButton(
            { navController.navigate(AppScreen.NewPatient) },
            AppScreen.NewPatient.toString()
        )
        if (demoMode) {
            MenuButton(
                { navController.navigate(AppScreen.Console) },
                AppScreen.Console.toString()
            )
            MenuButton(
                { navController.navigate(AppScreen.Demo) },
                AppScreen.Demo.toString()
            )
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