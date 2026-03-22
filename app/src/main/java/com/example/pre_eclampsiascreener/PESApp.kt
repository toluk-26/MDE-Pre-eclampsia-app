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
import com.example.pre_eclampsiascreener.ui.AppViewModel
import com.example.pre_eclampsiascreener.ui.components.Banner
import com.example.pre_eclampsiascreener.ui.screens.ConfigureScreen
import com.example.pre_eclampsiascreener.ui.screens.ConnectScreen
import com.example.pre_eclampsiascreener.ui.screens.MenuScreen
import com.example.pre_eclampsiascreener.ui.screens.NewPatientScreen
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import com.example.pre_eclampsiascreener.ui.viewmodels.ConsoleViewModel
import com.example.pre_eclampsiascreener.ui.viewmodels.DataViewModel

enum class AppScreen {
    DeviceConnection,
    Options,
    ViewData,
    Configure,
    NewPatient,
    Console,
    Demo,
    Calibrate;
    override fun toString(): String =
        when(this){
            DeviceConnection -> "Device Connection"
            ViewData -> "View Data"
            Configure -> "Configure"
            NewPatient -> "New Patient"
            Console -> "Console"
            Demo -> "Demo"
            else -> this.toString()
        }
}

@Composable
fun PESApp(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // how it goes back
    val backStackEntry by navController.currentBackStackEntryAsState()
    // remember the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.DeviceConnection.name
    )
    // sensor data
    val dataViewModel: DataViewModel = viewModel()
    // logs
    val consoleViewModel: ConsoleViewModel = viewModel()

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
                ConnectScreen(
                    onDeviceSelect = {
                        navController.navigate(AppScreen.Options.name)
                    }
                )
            }
            composable(route = AppScreen.Options.name){
                MenuScreen(
                    navController = navController
                )
            }
            composable(route = AppScreen.ViewData.name){

            }
            composable(route = AppScreen.Configure.name){
                ConfigureScreen(

                )
            }
            composable(route = AppScreen.NewPatient.name){
                NewPatientScreen(

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppTheme {
        PESApp()
    }
}