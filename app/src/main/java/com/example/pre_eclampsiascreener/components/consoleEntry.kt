package com.example.pre_eclampsiascreener.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun consoleEntryPreview() {
    Column {
        consoleEntry(1771558313, "This is an example msg")
        consoleEntry(1771558698, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam id laoreet mauris. Suspendisse")
    }

}

@Composable
fun consoleEntry(time: Int, msg: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text("$time: ",color = Color.White)
        Text(msg,color = Color.White)
    }

}