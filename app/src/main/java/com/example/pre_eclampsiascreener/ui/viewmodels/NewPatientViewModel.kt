package com.example.pre_eclampsiascreener.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pre_eclampsiascreener.data.PatientData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPatientViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PatientData())
    val uiState: StateFlow<PatientData> = _uiState.asStateFlow()

    fun idValue(): String {
        if(_uiState.value.patientId == null)
            return ""
        else
            return _uiState.value.patientId!!
    }

    fun setPatientId(pid: String){
        _uiState.update { currentState ->
            currentState.copy(patientId = pid)
        }
    }
}