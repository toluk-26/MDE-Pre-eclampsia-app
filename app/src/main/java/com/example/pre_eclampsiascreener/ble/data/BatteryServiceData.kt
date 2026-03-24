package com.example.pre_eclampsiascreener.ble.data

import com.example.pre_eclampsiascreener.ble.Profile

data class BatteryServiceData (
    override val profile: Profile = Profile.BATTERY,
    val batteryLevel: Int? = null,
): ProfileServiceData()