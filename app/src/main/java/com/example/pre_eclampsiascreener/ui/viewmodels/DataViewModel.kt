package com.example.pre_eclampsiascreener.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pre_eclampsiascreener.data.DataEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.plus

class DataViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<DataEntry>>(emptyList<DataEntry>())
    val items: StateFlow<List<DataEntry>> = _items.asStateFlow()

    fun add(item: DataEntry){
        viewModelScope.launch {
            _items.value += item
        }
    }

    fun listOfItems(): List<DataEntry> = items.value
}