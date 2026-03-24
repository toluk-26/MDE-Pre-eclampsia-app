package com.example.pre_eclampsiascreener.ble.parser

object BatteryLevelParser {

    fun parse(data: ByteArray): Int? =
        if (data.size == 1) data[0].toInt() and 0xFF else null
}
