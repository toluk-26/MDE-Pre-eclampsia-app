package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pre_eclampsiascreener.R
import com.example.pre_eclampsiascreener.ble.managers.CalibrateManager
import com.example.pre_eclampsiascreener.ble.repo.CalibrateRepository
import com.example.pre_eclampsiascreener.ui.demoScreens.DemoActigraphScreen
import com.example.pre_eclampsiascreener.ui.demoScreens.DemoBPWaitScreen
import com.example.pre_eclampsiascreener.ui.demoScreens.DemoBuzzerScreen
import com.example.pre_eclampsiascreener.ui.demoScreens.DemoStartScreen
import com.example.pre_eclampsiascreener.ui.demoScreens.DemoTransferScreen
import com.example.pre_eclampsiascreener.ui.viewmodels.DemoViewModel
import kotlinx.coroutines.launch

enum class DemoDestination(val route: String){
    start("1. Start"),
    actigraph("2. Preparing"),
    bp_read("3. Reading Blood Pressure"),
    transfer("4. Transferring Data"),
    buzzer("5. Alert!")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val startDestination = DemoDestination.start

    Scaffold(
        modifier = modifier,
        ) { contentPadding ->
        AppNavHost(navController, startDestination, modifier.padding(contentPadding))
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: DemoDestination,
    modifier: Modifier = Modifier,
    vm: DemoViewModel = viewModel()
) {

    // Observe the repository state
    val calibrateData by CalibrateRepository.data.collectAsState()

    // Map CalibrateServiceData.state values to destinations
    // Adjust these values to match your actual state integers/enum
    LaunchedEffect(calibrateData.state) {
        val destination = when (calibrateData.state) {
            1 -> DemoDestination.actigraph
            2 -> DemoDestination.bp_read
            3 -> DemoDestination.transfer
            4 -> DemoDestination.buzzer
            else -> null
        }
        destination?.let {
            navController.navigate(it.route) {
                launchSingleTop = true
                popUpTo(navController.currentDestination?.route ?: return@navigate) {
                    inclusive = true
                }
            }
        }
    }

val onResetClick = {
    navController.navigate(DemoDestination.start.route){
        popUpTo(0){inclusive = true}
    }
    vm.sendTrigger()
}

    NavHost(
        navController,
        startDestination = startDestination.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        }
    ) {
        DemoDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    DemoDestination.start -> DemoStartScreen(
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            navController.navigate(DemoDestination.actigraph.route)
                            vm.sendTrigger()
                        } // add .route
                    )
                    DemoDestination.actigraph -> DemoActigraphScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNext = { navController.navigate(DemoDestination.bp_read.route) },
                        onResetClick = { onResetClick() }
                    )
                    DemoDestination.bp_read -> DemoBPWaitScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNext = { navController.navigate(DemoDestination.transfer.route) },
                        onResetClick = { onResetClick() }
                    )
                    DemoDestination.transfer -> DemoTransferScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNext = { navController.navigate(DemoDestination.buzzer.route) },
                        onResetClick = { onResetClick() }
                    )
                    DemoDestination.buzzer -> DemoBuzzerScreen(
                        modifier = Modifier.fillMaxSize(),
                        onResetClick = { onResetClick() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewGraphTab(){
    val navController = rememberNavController()
    val startDestination = DemoDestination.start

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        AppNavHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewLiveTab(){
    val navController = rememberNavController()
    val startDestination = DemoDestination.actigraph

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        AppNavHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}