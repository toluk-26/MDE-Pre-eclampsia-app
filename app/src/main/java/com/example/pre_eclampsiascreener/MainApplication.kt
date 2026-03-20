package com.example.pre_eclampsiascreener

import android.app.Application
import android.util.Log
import no.nordicsemi.kotlin.ble.environment.android.NativeAndroidEnvironment

class MainApplication : Application() {
    lateinit var environment: NativeAndroidEnvironment
        private set

    override fun onCreate() {
        super.onCreate()
        Log.d("MainApplication", "Creating NativeAndroidEnvironment")
        environment = NativeAndroidEnvironment.getInstance(
            context = this,
            isNeverForLocationFlagSet = true   // we don't use BLE for location
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        environment.close()   // unregisters BT state BroadcastReceiver
    }
}