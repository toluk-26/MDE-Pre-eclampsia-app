package com.example.pre_eclampsiascreener.ble.parsers

object TimezoneParser {
    fun parse(data: ByteArray): Int? =
        if (data.size == 1) data[0].toInt() and 0xFF else null
}