package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.pre_eclampsiascreener.ble.repo.TransferRepository
import com.example.pre_eclampsiascreener.data.Payload
import com.example.pre_eclampsiascreener.ui.components.ConsoleCard

@Composable
fun ConsoleScreen(
    modifier: Modifier = Modifier
){
    val entries by TransferRepository.debug.collectAsState()
    LazyColumn(modifier) {
        items(entries
            .sortedBy { it.header.timestamp }
        ){ entry ->
            ConsoleCard(
                entry.header.timestamp,
                (entry.payload as? Payload.Debug)?.msg ?: ""
                )
        }
    }
}