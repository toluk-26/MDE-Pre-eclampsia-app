package com.example.pre_eclampsiascreener.ble.data

import com.example.pre_eclampsiascreener.ble.Profile

sealed class ProfileServiceData {
    abstract val profile: Profile
}