package com.example.pre_eclampsiascreener.data

sealed class Payload {
    data class Sensor(
        val data: SensorData
    ) : Payload()

    data class Debug(
        val msg: String?
    ) : Payload()
}
