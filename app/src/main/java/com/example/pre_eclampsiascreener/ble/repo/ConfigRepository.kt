package com.example.pre_eclampsiascreener.ble.repo

import com.example.pre_eclampsiascreener.ble.data.ConfigData
import com.example.pre_eclampsiascreener.ble.managers.ConfigManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ConfigRepository {
    private val _data = MutableStateFlow(ConfigData())
    val data: StateFlow<ConfigData> = _data.asStateFlow()

    fun updateDemoMode(flag: Boolean) =
        _data.update{it.copy(demoMode = flag)}

    fun updatePID(pid: UInt?) =
        _data.update{it.copy(pid = pid)}

    fun updateDiastolic(list: List<Int>) =
        _data.update{it.copy(diastolicMin = list[0], diastolicMax = list[1])}

    fun updateDiastolicCoefficients(list: List<Float>) =
        _data.update{it.copy(diastolicCoeff_m = list[0], diastolicCoeff_b = list[1])}

    fun updateSystolic(list: List<Int>) =
        _data.update{it.copy(systolicMin = list[0], systolicMax = list[1])}

    fun updateSystolicCoefficients(list: List<Float>) =
        _data.update{it.copy(systolicCoeff_m = list[0], systolicCoeff_b = list[1])}

    suspend fun startNewPatient()=
        ConfigManager.writeNewPatient()

    suspend fun writePID(pid: Int) =
        ConfigManager.writePID(pid)

    suspend fun writeDiastolic(min: Int, max: Int) =
        ConfigManager.writeDiastolic(listOf(min, max))

    suspend fun writeDiastolicCoefficients(m: Float, b: Float) =
        ConfigManager.writeDiastolicCoefficients(listOf(m, b))

    suspend fun writeSystolic(min: Int, max: Int) =
        ConfigManager.writeSystolic(listOf(min, max))

    suspend fun writeSystolicCoefficients(m: Float, b: Float) =
        ConfigManager.writeSystolicCoefficients(listOf(m, b))

    fun clear() {
        _data.update { ConfigData() }
    }
}