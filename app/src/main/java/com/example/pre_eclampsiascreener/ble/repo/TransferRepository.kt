package com.example.pre_eclampsiascreener.ble.repo

import android.util.Log
import com.example.pre_eclampsiascreener.ble.data.TransferServiceData
import com.example.pre_eclampsiascreener.ble.managers.TransferManager
import com.example.pre_eclampsiascreener.data.PayloadType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TransferRepository {
    private val _sensor = MutableStateFlow<List<TransferServiceData>>(emptyList())
    val sensor: StateFlow<List<TransferServiceData>> = _sensor.asStateFlow()

    private val _debug = MutableStateFlow<List<TransferServiceData>>(emptyList())
    val debug: StateFlow<List<TransferServiceData>> = _debug.asStateFlow()

    private var tries = 0

    fun trigger(){
        if (tries > 3){
            Log.w("TransferRepository", "Too many data transfer attempts")
        }
        clear()
        TransferManager.triggerTransfer()
    }

    fun add(newData: TransferServiceData) {
        when (newData.header.type) {
            PayloadType.SENSOR -> _sensor.update { it + newData }
            PayloadType.DEBUG -> _debug.update { it + newData }
        }
    }

    suspend fun checkSize(size: Int){
        val actualSize = _debug.value.size + _sensor.value.size
        if (size != (_debug.value.size + _sensor.value.size))
        if (size != actualSize) {
            Log.e("TransferRepo", "Wrong Size! Size is $size but only have $actualSize")
            trigger()
            tries++
        }
        else Log.d("TransferRepo", "Size is $size")
        tries = 0
    }

    fun clear() {
        _sensor.update { emptyList() }
        _debug.update { emptyList() }
        tries = 0
    }
}