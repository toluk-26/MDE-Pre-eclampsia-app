@file:OptIn(ExperimentalUuidApi::class)

package com.example.pre_eclampsiascreener.ble

import com.example.pre_eclampsiascreener.ble.managers.BatteryManager
import com.example.pre_eclampsiascreener.ble.managers.TimeManager
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class Profile (
    val descriptor: String,
    val uuid: Uuid,
    val createManager: () -> ServiceManager
) {
    BATTERY(
        "Battery",
        Uuid.parse(BATTERY_SERVICE_UUID),
        ::BatteryManager
    ),
    TIME(
        "Time",
        Uuid.parse(TIME_SERVICE_UUID),
        ::TimeManager
    ),
    // TODO: add services here
    ;

    override fun toString(): String {
        return descriptor
    }
}

const val BATTERY_SERVICE_UUID: String = "0000180F-0000-1000-8000-00805f9b34fb"
const val TIME_SERVICE_UUID: String = "043f0000-0ff5-45d1-9502-db9d40757da2"
// TODO: add services here