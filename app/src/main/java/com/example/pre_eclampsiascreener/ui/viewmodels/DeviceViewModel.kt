package com.example.pre_eclampsiascreener.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pre_eclampsiascreener.data.DeviceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<DeviceInfo>>(emptyList<DeviceInfo>())
    val items: StateFlow<List<DeviceInfo>> = _items.asStateFlow()

    fun add(item: DeviceInfo){
        viewModelScope.launch {
            _items.value += item
        }
    }

    fun listOfDevices(): List<DeviceInfo> = items.value

}