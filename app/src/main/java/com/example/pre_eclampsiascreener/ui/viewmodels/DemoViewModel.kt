package com.example.pre_eclampsiascreener.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pre_eclampsiascreener.ble.repo.CalibrateRepository
import kotlinx.coroutines.launch

class DemoViewModel: ViewModel()  {
    fun sendTrigger()=
        viewModelScope.launch { CalibrateRepository.triggerDemo() }
}