package com.example.pre_eclampsiascreener.ble.repo

import com.example.pre_eclampsiascreener.ble.data.TimeServiceData
import com.example.pre_eclampsiascreener.ble.managers.TimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object TimeRepository {
    private val _data = MutableStateFlow(TimeServiceData())

    fun updateTime(time: Long){
        _data.update { it.copy(unixTime = time) }
    }

    fun updateTimezone(tz: Int){
        _data.update { it.copy(timezone = tz) }
    }

    suspend fun writeTime(time: Long){
        TimeManager.writeTime(time)
    }

    suspend fun writeTimezone(tx: Int){

    }

    fun clear() {
        _data.update {
            it.copy(
                unixTime = null,
                timezone = null
            )
        }
    }
}