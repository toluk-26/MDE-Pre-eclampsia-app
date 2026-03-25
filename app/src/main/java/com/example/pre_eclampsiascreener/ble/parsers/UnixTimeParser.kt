package com.example.pre_eclampsiascreener.ble.parsers

object UnixTimeParser {
    fun parse(data: ByteArray): Long? =
        if (data.size == 8)
            (data[0].toLong() and 0xFF) or
                    (data[1].toLong() and 0xFF shl 8) or
                    (data[2].toLong() and 0xFF shl 16) or
                    (data[3].toLong() and 0xFF shl 24) or
                    (data[4].toLong() and 0xFF shl 32) or
                    (data[5].toLong() and 0xFF shl 40) or
                    (data[6].toLong() and 0xFF shl 48) or
                    (data[7].toLong() and 0xFF shl 56)
        else null

}