package com.example.pre_eclampsiascreener.ble.parsers

import com.example.pre_eclampsiascreener.ble.data.TransferServiceData
import com.example.pre_eclampsiascreener.data.Payload
import com.example.pre_eclampsiascreener.data.PayloadType
import com.example.pre_eclampsiascreener.data.SensorData
import com.example.pre_eclampsiascreener.data.TransferHeaderData
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val HEADER_SIZE = 13
private const val SENSOR_SIZE = 4

fun ByteArray.parseTransferData(): TransferServiceData? {
    val header: TransferHeaderData = this.copyOfRange(0, HEADER_SIZE).toHeader() ?: return null

    return when (header.type) {
        PayloadType.SENSOR -> TransferServiceData(
            header = header,
            payload = Payload.Sensor(this.toSensorData() ?: return null)
        )

        else -> TransferServiceData(
            header = header,
            payload = Payload.Debug(this.toString(Charsets.UTF_8))
        )
    }
}

private fun ByteArray.toHeader(): TransferHeaderData? {
    if (size != HEADER_SIZE) return null
    val buf = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)
    return TransferHeaderData(
        type = buf.get().toPayloadType(),
        length = buf.int.toUInt(),
        timestamp = buf.long
    )
}

private fun Byte.toPayloadType(): PayloadType =
    when (this.toInt()) {
        0 -> PayloadType.SENSOR
        else -> PayloadType.DEBUG
    }

private fun ByteArray.toSensorData(): SensorData? {
    if (size < SENSOR_SIZE) return null
    val buf = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN)
    return SensorData(
        buf.get().toInt(),
        buf.get().toInt(),
        buf.get().toInt(),
        buf.get().toUInt(),
    )
}

fun ByteArray.toInt(): Int?{
    if (size != 4 ) return null
    return ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).int
}