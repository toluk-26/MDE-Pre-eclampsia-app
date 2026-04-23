@file:OptIn(ExperimentalUuidApi::class)

package com.example.pre_eclampsiascreener.ble

import com.example.pre_eclampsiascreener.ble.managers.BatteryManager
import com.example.pre_eclampsiascreener.ble.managers.CalibrateManager
import com.example.pre_eclampsiascreener.ble.managers.ConfigManager
import com.example.pre_eclampsiascreener.ble.managers.DeviceInfoManager
import com.example.pre_eclampsiascreener.ble.managers.TimeManager
import com.example.pre_eclampsiascreener.ble.managers.TransferManager
import com.example.pre_eclampsiascreener.ble.repo.BatteryRepository
import com.example.pre_eclampsiascreener.ble.repo.CalibrateRepository
import com.example.pre_eclampsiascreener.ble.repo.ConfigRepository
import com.example.pre_eclampsiascreener.ble.repo.DeviceInfoRepository
import com.example.pre_eclampsiascreener.ble.repo.TimeRepository
import com.example.pre_eclampsiascreener.ble.repo.TransferRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class Profile (
    val descriptor: String,
    val uuid: Uuid,
    val createManager: () -> ServiceManager,
    val clearRepository: () -> Unit
) {
    CONFIG(
        "Configuration",
        Uuid.parse(CONFIG_SERVICE_UUID),
        ::ConfigManager,
        { ConfigRepository.clear() }
    ),
    BATTERY(
        "Battery",
        Uuid.parse(BATTERY_SERVICE_UUID),
        ::BatteryManager,
        { BatteryRepository.clear() }
    ),
    DEVICE_INFORMATION(
        "Device Information",
        Uuid.parse(DEV_INFO_SERVICE_UUID),
        ::DeviceInfoManager,
        { DeviceInfoRepository.clear() }
    ),
    TIME(
        "Time",
        Uuid.parse(TIME_SERVICE_UUID),
        ::TimeManager,
        { TimeRepository.clear() }
    ),
    TRANSFER(
        "Transfer",
        Uuid.parse(TRANSFER_SERVICE_UUID),
        ::TransferManager,
        { TransferRepository.clear() }
    ),
    CALIBRATE(
        "Calibrate",
        Uuid.parse(CALIBRATE_SERVICE_UUID),
        ::CalibrateManager,
        { CalibrateRepository.clear()}
    )

    // TODO: add services here
    ;

    override fun toString(): String {
        return descriptor
    }
}

const val DEV_INFO_SERVICE_UUID: String = "0000180A-0000-1000-8000-00805f9b34fb"
const val BATTERY_SERVICE_UUID: String = "0000180F-0000-1000-8000-00805f9b34fb"
const val TIME_SERVICE_UUID: String = "043f0000-7bdb-4430-a1b9-e7d26fb2b981"
const val CONFIG_SERVICE_UUID: String = "32610000-7bdb-4430-a1b9-e7d26fb2b981"
const val TRANSFER_SERVICE_UUID: String = "8d760000-7bdb-4430-a1b9-e7d26fb2b981"
const val CALIBRATE_SERVICE_UUID: String = "c16e0000-7bdb-4430-a1b9-e7d26fb2b981"
// TODO: add services here
