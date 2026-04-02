package com.example.pre_eclampsiascreener.ble.repo

import android.util.Log
import com.example.pre_eclampsiascreener.ble.data.TimeServiceData
import com.example.pre_eclampsiascreener.ble.managers.TimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TimeRepository {
    private const val TAG = "TimeRepository"

    private val _data = MutableStateFlow(TimeServiceData())
    val data: StateFlow<TimeServiceData> = _data.asStateFlow()

    fun updateTime(time: Long){
        _data.update { it.copy(unixTime = time) }
    }

    fun updateTimezone(tz: Int){
        _data.update { it.copy(timezone = tz) }
    }

    suspend fun writeTime(time: Long){
        TimeManager.writeTime(time)
    }

    suspend fun writeTimezone(tz: Int){
        TimeManager.writeTz(tz.toByte())
        Log.d(TAG, "trying to print $tz -> ${tz.toByte()}")
    }

    fun clear() =
        _data.update { TimeServiceData() }
}