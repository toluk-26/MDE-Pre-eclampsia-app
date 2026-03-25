package com.example.pre_eclampsiascreener.ble.data

import com.example.pre_eclampsiascreener.ble.Profile

data class TimeServiceData (
    override val profile: Profile = Profile.TIME,
    val unixTime: Long? = null,
    val timezone: Int? = null
): ProfileServiceData()