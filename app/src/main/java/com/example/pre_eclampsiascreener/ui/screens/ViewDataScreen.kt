package com.example.pre_eclampsiascreener.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pre_eclampsiascreener.data.DataEntry
import com.example.pre_eclampsiascreener.ui.theme.AppTheme
import com.example.pre_eclampsiascreener.ui.viewmodels.ConfigureViewModel
import com.example.pre_eclampsiascreener.ui.viewmodels.DataViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.decoration.Decoration
import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.LegendItem
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.Position
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.rememberVerticalLegend
import com.patrykandpatrick.vico.compose.common.vicoTheme
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.forEachIndexed
import androidx.compose.runtime.collectAsState


@Composable
fun ViewDataScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
) {
    val data = dataViewModel.listOfItems()
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(data.map { it.time }, data.map { it.systolic })
                series(data.map { it.time }, data.map { it.diastolic })
            }
        }
    }

//    Scaffold(
//        floatingActionButton = { GoToConfigureAction() }
//    ) { innerPadding ->
//        LineChart(
//            modelProducer,
//            Modifier.padding(innerPadding)
//        )
//    }
}

@Composable
private fun ComposeBasicLineChart(
    modelProducer: CartesianChartModelProducer,
    decorations: List<Decoration>,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = CartesianValueFormatter { _, value, _ ->
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        sdf.format(Date(value.toLong() * 1000)) // convert unix seconds → ms
    }
    val lineColors = listOf(Color(0xff916cda), Color(0xffd877d8))
    val lineNames = listOf("Systolic", "Diastolic")
    val lineProvider = LineCartesianLayer.LineProvider.series(
        lineColors.map { color ->
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(Fill(color)),
                areaFill = null,
                pointProvider =
                    LineCartesianLayer.PointProvider.single(
                        LineCartesianLayer.Point(rememberShapeComponent(Fill(color), CircleShape))
                    ),
            )
        }
    )
    val legendText = rememberTextComponent(
        TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
    )
    val legendItemLabelComponent = rememberTextComponent(TextStyle(vicoTheme.textColor, 12.sp))

    CartesianChartHost(
        chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = lineProvider
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = dateFormatter
                ),
//                marker = rememberMarker()
            legend = rememberVerticalLegend(
                    items = {
                        lineNames.forEachIndexed { index, label ->
                            add(
                                LegendItem(
                                    ShapeComponent(Fill(lineColors[index]), CircleShape),
                                    legendItemLabelComponent,
                                    label,
                                )
                            )
                        }
                    },
                    padding = Insets(top = 16.dp),
                ),
            decorations = decorations
            ),

        modelProducer = modelProducer,
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun ComposeBasicLineChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }

    val vm: DataViewModel = viewModel()
    vm.add(DataEntry(1772610341, 1, 100, 55))
    vm.add(DataEntry(1772610343, 2, 105, 60))
    vm.add(DataEntry(1772610345, 3, 115, 80))
    vm.add(DataEntry(1772610347, 2, 108, 78))
    val data = vm.listOfItems()

    val conf: ConfigureViewModel = viewModel()
    conf.setMaxBloodPressure("150/50")
    conf.setMinBloodPressure("90/30")
    val perf = conf.UiState.collectAsState()
    val decorations = listOf(
        rememberHorizontalLine("max Diastolic", perf.value.maxDiastolic?.toDouble() ?: 0.0),
        rememberHorizontalLine("min Diastolic", perf.value.minDiastolic?.toDouble() ?: 0.0),
        rememberHorizontalLine("max Systolic", perf.value.maxSystolic?.toDouble() ?: 0.0),
        rememberHorizontalLine("min Systolic", perf.value.minSystolic?.toDouble() ?: 0.0)
    )


    runBlocking {
        modelProducer.runTransaction {
            lineSeries {
                series(data.map { it.time }, data.map { it.systolic })
                series(data.map { it.time }, data.map { it.diastolic })
            }
        }
    }
    AppTheme {
        ComposeBasicLineChart(modelProducer, decorations)
    }
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
                rememberShapeComponent(
                    fill,
                    RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                ),
        )
    return remember {
        HorizontalLine(
            y = { y },
            line = line,
            labelComponent = labelComponent,
            label = { "" },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}


@Composable
fun DataTable(
    modifier: Modifier = Modifier,
    data: List<DataEntry>,
) {

}

@Composable
fun GoToConfigureAction(

) {

}

@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = MarkerCornerBasedShape(CircleShape)
    val labelBackground =
        rememberShapeComponent(
            fill = Fill(MaterialTheme.colorScheme.background),
            shape = labelBackgroundShape,
            strokeFill = Fill(MaterialTheme.colorScheme.outline),
            strokeThickness = 1.dp,
        )
    val label =
        rememberTextComponent(
            style =
                TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                ),
            padding = Insets(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(Fill(MaterialTheme.colorScheme.surface), CircleShape)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
            if (showIndicator) {
                { color ->
                    LayeredComponent(
                        back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CircleShape),
                        front =
                            LayeredComponent(
                                back = ShapeComponent(fill = Fill(color), shape = CircleShape),
                                front = indicatorFrontComponent,
                                padding = Insets(5.dp),
                            ),
                        padding = Insets(10.dp),
                    )
                }
            } else {
                null
            },
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}