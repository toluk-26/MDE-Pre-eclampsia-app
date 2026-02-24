package com.example.pre_eclampsiascreener.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.pre_eclampsiascreener.console.ConsoleItem
import com.example.pre_eclampsiascreener.console.ConsoleItems
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Preview
@Composable
fun ConsoleCardPreview() {
    ConsoleCard(ConsoleItem(1771558313, 0))

}

@OptIn(ExperimentalTime::class)
@Composable
fun ConsoleCard(entry: ConsoleItem) {
    val instant = Instant.fromEpochSeconds(entry.unixTime)
    val utc = instant.toLocalDateTime(TimeZone.UTC)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text("$utc: ",color = Color.White)
        Text(entry.msgCode.toString(), color = Color.White) // TODO: implement json
    }
}

@Composable
fun ConsoleList(consoleEntries: List<ConsoleItem>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier) {
        items(consoleEntries){ entry ->
            ConsoleCard(entry)
        }
    }
}

@Preview
@Composable
fun ConsoleListPreview() {
    ConsoleItems.add(ConsoleItem(1771558313, 0))
    ConsoleItems.add(ConsoleItem(1771558314, 0))
    ConsoleItems.add(ConsoleItem(1771558315, 0))
    ConsoleItems.add(ConsoleItem(1771558316, 0))

    ConsoleList(
        ConsoleItems.listOfConsoleItems(),
        Modifier
            .fillMaxWidth()
    )

}