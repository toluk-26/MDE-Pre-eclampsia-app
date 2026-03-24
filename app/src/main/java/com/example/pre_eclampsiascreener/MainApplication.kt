package com.example.pre_eclampsiascreener

import android.app.Application
import android.util.Log
import com.example.pre_eclampsiascreener.ble.BleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment

class MainApplication : Application() {
    lateinit var environment: NativeAndroidEnvironment
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    lateinit var bleManager: BleManager
        private set

    override fun onCreate() {
        super.onCreate()
        Log.d("MainApplication", "Creating NativeAndroidEnvironment")
        environment = NativeAndroidEnvironment.getInstance(
            context = this,
            isNeverForLocationFlagSet = true   // we don't use BLE for location
        )
        bleManager = BleManager(environment, appScope)
    }

    override fun onTerminate() {
        super.onTerminate()
        environment.close()   // unregisters BT state BroadcastReceiver
        bleManager.close()
        appScope.cancel()
    }
}