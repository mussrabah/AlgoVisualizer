package com.muss_coding.algovisualizer.presentation.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme
import kotlin.random.Random

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    state: OnBoardingState,
    onAction: (OnBoardingEvents) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextField(
            value = if (state.iteration == 0) "" else state.iteration.toString(),
            onValueChange = {
                onAction(OnBoardingEvents.OnIterationChanged(it.toIntOrNull() ?: 0))
            },
            label = { Text("Iterations") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = state.selectedOption == 1,
                onClick = { onAction(OnBoardingEvents.OnOptionSelected(1)) }
            )
            Text(
                text = "Selection Sort",
            )
        }

        Button(
            onClick = {
                onAction(OnBoardingEvents.OnStart)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Start")
        }



        if (state.shouldSowBars) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val displayMetrics = LocalContext.current.resources.displayMetrics
                val width = displayMetrics.widthPixels / displayMetrics.density
                val widthPerEach = width / (state.iteration + 2)
                state.bars.forEach { model ->
                    Column(
                        modifier = Modifier
                            .width(widthPerEach.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val selectedBar = state.selectedBar?.equals(model)
                        val comparedTo = state.comparedToBar?.equals(model)
                        if (selectedBar == true || comparedTo == true) {
                            val color = if (selectedBar == true) Color.Black else Color.Green
                            Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                                drawLine(
                                    color = color,
                                    start = Offset(size.width/2, 0f),
                                    end = Offset(size.width/2, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )

                                drawLine(
                                    color = color,
                                    start = Offset(0f, 2*size.height/3),
                                    end = Offset(size.width/2, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )

                                drawLine(
                                    color = color,
                                    start = Offset(size.width, 2*size.height/3),
                                    end = Offset(size.width/2, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(model.color)
                                .width(widthPerEach.dp)
                                .height(model.value.times(10).dp)
                        ) {
                            Text(
                                text = model.name,
                                modifier = Modifier.align(Alignment.Center).rotate(90f),
                                maxLines = 2,
                                color = Color.White
                            )
                        }

                    }
                }
            }
        }
    }
}

data class ArrayModel(
    val name: String,
    val value: Int,
    val color: Color,
    val selected: Boolean = false,
    val beingCompared: Boolean = false,
) {
    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayModel

        if (value != other.value) return false
        if (selected != other.selected) return false
        if (beingCompared != other.beingCompared) return false
        if (name != other.name) return false
        if (color != other.color) return false

        return true
    }
}

@Composable
@PreviewLightDark
fun OnBoardingScreenPreview() {
    AlgoVisualizerTheme {
        OnBoardingScreen(
            state = OnBoardingState(shouldSowBars = true),
            onAction = {}
        )

    }
}