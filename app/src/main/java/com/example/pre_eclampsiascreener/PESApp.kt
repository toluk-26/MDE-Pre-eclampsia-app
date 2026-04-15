package com.example.pre_eclampsiascreener

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pre_eclampsiascreener.ble.repo.BatteryRepository
import com.example.pre_eclampsiascreener.ble.repo.ConfigRepository
import com.example.pre_eclampsiascreener.ble.repo.DeviceInfoRepository
import com.example.pre_eclampsiascreener.ui.AppViewModel
import com.example.pre_eclampsiascreener.ui.components.Banner
import com.example.pre_eclampsiascreener.ui.components.DeviceInfoBanner
import com.example.pre_eclampsiascreener.ui.screens.ConfigureScreen
import com.example.pre_eclampsiascreener.ui.screens.ConnectScreen
import com.example.pre_eclampsiascreener.ui.screens.ConsoleScreen
import com.example.pre_eclampsiascreener.ui.screens.DemoScreen
import com.example.pre_eclampsiascreener.ui.screens.MenuScreen
import com.example.pre_eclampsiascreener.ui.screens.NewPatientScreen
import com.example.pre_eclampsiascreener.ui.screens.ViewDataScreen
import com.example.pre_eclampsiascreener.ui.theme.AppTheme

enum class AppScreen {
    DeviceConnection, Options, ViewData, Configure, NewPatient, Console, Demo, Calibrate;

    override fun toString(): String = when (this) {
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
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val isConnectionScreen = currentRoute == AppScreen.DeviceConnection.name

    Scaffold(
        topBar = {
            Column() {
                Banner {}   // your top banner
                if (!isConnectionScreen) {
                    // Show device info only when connected
                    val batteryLevel by BatteryRepository.data.collectAsState()
                    val config by ConfigRepository.data.collectAsState()
                    val devInfo by DeviceInfoRepository.deviceName.collectAsState()

                    DeviceInfoBanner(devInfo, config.pid.toString(), batteryLevel.batteryLevel)
                }
            }
        }
    ) { innerPadding ->
//        Column {
//                DeviceInfoBanner("pissin", "123456789", 11)

            NavHost(
                navController = navController,
                startDestination = AppScreen.DeviceConnection.name,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()           // ← Important
            ) {

                composable(route = AppScreen.DeviceConnection.name) {
                    ConnectScreen(
                        onSuccess = {
                            navController.navigate(AppScreen.Options.name)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(route = AppScreen.Options.name) {
                    val config by ConfigRepository.data.collectAsState()
                    MenuScreen(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        demoMode = config.demoMode
                    )
                }
                composable(route = AppScreen.ViewData.name) {
                    ViewDataScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(route = AppScreen.Configure.name) {
                    ConfigureScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(route = AppScreen.NewPatient.name) {
                    NewPatientScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(route = AppScreen.Console.name) {
                    ConsoleScreen(
                        Modifier.fillMaxSize()
                    )
                }
                composable(route = AppScreen.Demo.name) {
                    DemoScreen(
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
//    }

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppTheme {
        PESApp()
    }
}