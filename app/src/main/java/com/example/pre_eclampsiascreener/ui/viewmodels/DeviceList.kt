package com.example.pre_eclampsiascreener.ui.viewmodels

import com.example.pre_eclampsiascreener.data.DeviceInfo

class DeviceList {
    val devices = mutableListOf<DeviceInfo>()

    fun add(device: DeviceInfo){
        devices.add(device)
    }

    fun listOfDevices(): List<DeviceInfo>{
        return devices
    }
}