package com.example.pre_eclampsiascreener.ble.parsers

import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val SIZE_OF_2_BYTES = 2 * Byte.SIZE_BYTES
private const val SIZE_OF_2_FLOATS = 2 * Float.SIZE_BYTES

fun ByteArray.toBoolean(): Boolean {
    Log.d("parser", "Boolean Input: ${this.contentToString()}")
    return isNotEmpty() && this[0] != 0.toByte()
}

fun ByteArray.toMinMaxList(): List<Int>{
    if (size != SIZE_OF_2_BYTES) {
        Log.e("parser", "Bad size: $size")
        return emptyList()
    }
    val buf = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)

    return listOf(
        buf.get().toInt(),
        buf.get().toInt(),
    )
}

fun ByteArray.toFloatList(): List<Float>{
    if (size != SIZE_OF_2_FLOATS) return emptyList()
    val buf = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)

    return listOf(
        buf.getFloat(),
        buf.getFloat(),
    )
}

fun Int.toByteArray(): ByteArray =
    ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()

fun Float.toByteArray(): ByteArray =
    ByteBuffer.allocate(Float.SIZE_BYTES).putFloat(this).array()

