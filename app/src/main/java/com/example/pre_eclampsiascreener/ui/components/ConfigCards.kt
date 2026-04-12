package com.example.pre_eclampsiascreener.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ThresholdCard(
    minBloodValue: String,
    maxBloodValue: String,
    minBloodChange: (String) -> Unit,
    maxBloodChange: (String) -> Unit
){
    // Card section
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Blood Pressure Thresholds",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = minBloodValue,
                    onValueChange = minBloodChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Minimum") },
                    placeholder = { Text("90/60") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = maxBloodValue,
                    onValueChange = maxBloodChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Maximum") },
                    placeholder = { Text("120/80") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
fun MinMaxRow(
    modifier: Modifier = Modifier,
    minValueChange: (String) -> Unit,
    minValue: String,
    maxValueChange: (String) -> Unit,
    maxValue: String,
    maxKeyBoardAction: ImeAction
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            value = minValue,
            onValueChange = minValueChange,
            label = { Text("min") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )
        TextField(
            value = maxValue,
            onValueChange = maxValueChange,
            label = { Text("max") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = maxKeyBoardAction
            )
        )
    }
}