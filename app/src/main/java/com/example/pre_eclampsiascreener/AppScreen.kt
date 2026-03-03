package com.example.pre_eclampsiascreener

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pre_eclampsiascreener.data.DeviceInfo
import com.example.pre_eclampsiascreener.ui.AppViewModel
import com.example.pre_eclampsiascreener.ui.components.Banner
import com.example.pre_eclampsiascreener.ui.screens.DeviceSelectionScreen
import com.example.pre_eclampsiascreener.ui.theme.AppTheme

enum class AppScreen {
    DeviceConnection,
    Options,
    ViewData,
    Configure,
    NewPatient,
    Console,
    Demo,
    Calibrate
}

@Composable
fun AppScreen(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // how it goes back
    val backStackEntry by navController.currentBackStackEntryAsState()
    // remember the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.DeviceConnection.name
    )

    Scaffold(
        topBar = {
            Banner(
            {}
        )
        }
    ) { innerPadding ->
        NavHost(
            navController,
            AppScreen.DeviceConnection.name,
            Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.DeviceConnection.name){
                DeviceSelectionScreen()
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppTheme {
        AppScreen()
    }
}