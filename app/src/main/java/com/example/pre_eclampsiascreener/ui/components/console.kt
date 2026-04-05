package com.example.pre_eclampsiascreener.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun ConsoleCard(
    time: Long,
    code: Int = 0,
    msg: String?,
) {
    val instant = Instant.fromEpochSeconds(time)
    val utc = instant.toLocalDateTime(TimeZone.UTC)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Text("$utc: ",color = Color.White)
        Text(msg ?: "NO MSG", color = Color.White) // TODO: implement json
    }
}

@Preview
@Composable
fun ConsoleCardPreview() {
    ConsoleCard(1771558313, -1, "0")
}