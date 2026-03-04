package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pre_eclampsiascreener.data.DataEntry
import com.example.pre_eclampsiascreener.ui.viewmodels.DataViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.Position
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import kotlinx.coroutines.runBlocking


@Composable
fun ViewDataScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
){
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/z5ah6v.
            lineSeries { series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11) }
        }
    }

    Scaffold(
        floatingActionButton = {GoToConfigureAction()}
    ) { innerPadding ->
        LineChart(
            modelProducer,
            Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    baseTime: Long? = null, // pass this if using normalized time
) {
//    val bottomFormatter = remember(baseTime) {
//        CartesianValueFormatter { _, x, _ ->
//            baseTime?.let {
//                val timestamp = it + x.toLong()
//                SimpleDateFormat("HH:mm", Locale.getDefault())
//                    .format(Date(timestamp * 1000))
//            } ?: x.toString()
//        }
//    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
//                valueFormatter = bottomFormatter
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}


//@Preview(showBackground = true)
//@Composable
//fun LineChartPreview() {
//    val modelProducer = remember { CartesianChartModelProducer() }
//
//    // Sample data (replace with real data)
//    val data = listOf(
//        DataEntry(1700000000, 1, 80, 120),
//        DataEntry(1700000600, 2, 82, 122),
//        DataEntry(1700001200, 1, 78, 118),
//    )
//
//    runBlocking?.invoke {
//        modelProducer.runTransaction {
//            lineSeries {
//                series(
//                    x = data.map { it.time.toFloat() },
//                    y = data.map { it.diastolic.toFloat() }
//                )
//                series(
//                    x = data.map { it.time.toFloat() },
//                    y = data.map { it.systolic.toFloat() }
//                )
//            }
//        }
//    }
//
//    AppTheme {
//        LineChart(
//            modelProducer = modelProducer
//        )
//    }
//
//
//}

@Composable
private fun ComposeBasicLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
fun ComposeBasicLineChart(modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/z5ah6v.
            lineSeries { series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11) }
        }
    }
    ComposeBasicLineChart(modelProducer, modifier)
}

@Composable
@Preview
private fun ComposeBasicLineChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }

    val vm: DataViewModel = viewModel()
    vm.add(DataEntry(1772610341, 1, 100, 55))
    vm.add(DataEntry(1772610343, 2, 105, 60))
    vm.add(DataEntry(1772610345, 3, 115, 80))
    vm.add(DataEntry(1772610347, 2, 108, 78))
    val data = vm.listOfItems()

    runBlocking {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/z5ah6v.
            lineSeries {
//                series(data.)
            }
        }
    }
    ComposeBasicLineChart(modelProducer)
}

@Composable
private fun rememberHorizontalLine(
    label: String,
    y: Double
): HorizontalLine {
    val fill = Fill(Color(0xfffdc8c4))
    val line = rememberLineComponent(fill = fill, thickness = 2.dp)
    val labelComponent =
        rememberTextComponent(
            margins = Insets(start = 6.dp),
            padding = Insets(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 4.dp),
            background =
                rememberShapeComponent(fill, RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)),
        )
    return remember {
        HorizontalLine(
            y = { y },
            line = line,
            labelComponent = labelComponent,
            label = { label },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}


@Composable
fun DataTable(
    modifier: Modifier = Modifier,
    data: List<DataEntry>,
){

}

@Composable
fun GoToConfigureAction(

){

}