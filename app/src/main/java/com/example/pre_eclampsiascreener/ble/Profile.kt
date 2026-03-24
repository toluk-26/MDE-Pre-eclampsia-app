package com.example.pre_eclampsiascreener.ble

import com.example.pre_eclampsiascreener.ble.managers.BatteryManager
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class Profile @OptIn(ExperimentalUuidApi::class) constructor(
    val descriptor: String,
    val uuid: Uuid,
    val createManager: () -> ServiceManager
) {
    @OptIn(ExperimentalUuidApi::class)
    BATTERY(
        "Battery",
        Uuid.parse(BATTERY_SERVICE_UUID),
        ::BatteryManager
    ),
    // TODO: add services here
    ;

    override fun toString(): String {
        return descriptor
    }
}

const val BATTERY_SERVICE_UUID: String = "0000180F-0000-1000-8000-00805f9b34fb"
// TODO: add services here