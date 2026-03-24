package com.example.pre_eclampsiascreener.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pre_eclampsiascreener.data.ConfigureData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConfigureData())
    val uiState: StateFlow<ConfigureData> = _uiState.asStateFlow()

    fun setPatientId(pid: String){
        _uiState.update { currentState ->
            currentState.copy(patientID = pid)
        }
    }

    fun setMinBloodPressure(value: String){
        val (sys, dia) = parseBloodPressure(value)
        setMinSystolic(sys)
        setMinDiastolic(dia)
    }

    fun setMaxBloodPressure(value: String){
        val (sys, dia) = parseBloodPressure(value)
        setMaxSystolic(sys)
        setMaxDiastolic(dia)
    }

    fun minBloodPressure(): String {
        val sys = uiState.value.minSystolic
        val dia = uiState.value.minDiastolic

        return if (sys == null && dia == null) {
            ""
        } else {
            "${sys ?: ""}/${dia ?: ""}"
        }
    }

    fun maxBloodPressure(): String {
        val sys = uiState.value.maxSystolic
        val dia = uiState.value.maxDiastolic

        return if (sys == null && dia == null) {
            ""
        } else {
            "${sys ?: ""}/${dia ?: ""}"
        }
    }

    private fun setMinDiastolic(value: Int?){
        _uiState.update { currentState ->
            currentState.copy(minDiastolic = value)
        }
    }
    private fun setMinSystolic(value: Int?){
        _uiState.update { currentState ->
            currentState.copy(minSystolic = value)
        }
    }
    private fun setMaxDiastolic(value: Int?){
        _uiState.update { currentState ->
            currentState.copy(maxDiastolic = value)
        }
    }
    private fun setMaxSystolic(value: Int?){
        _uiState.update { currentState ->
            currentState.copy(maxSystolic = value)
        }
    }

    private fun parseBloodPressure(input: String): Pair<Int?, Int?> {
        val parts = input.split("/")

        val systolic = parts.getOrNull(0)?.trim()?.toIntOrNull()
        val diastolic = parts.getOrNull(1)?.trim()?.toIntOrNull()

        return systolic to diastolic
    }
}