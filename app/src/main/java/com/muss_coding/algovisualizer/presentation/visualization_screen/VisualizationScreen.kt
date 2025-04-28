package com.muss_coding.algovisualizer.presentation.visualization_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muss_coding.algovisualizer.R
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationUI
import com.muss_coding.algovisualizer.presentation.configuration_screen.components.SliderWithLabel
import com.muss_coding.algovisualizer.presentation.visualization_screen.components.SortingVisualizer

@Composable
fun VisualizationScreen(
    modifier: Modifier = Modifier,
    state: VisualizationState,
    onAction: (VisualizationAction) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onAction(VisualizationAction.OnPlayClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Add"
                        )
                    }
                },
                actions = {
                    OutlinedIconButton (
                        onClick = {
                            onAction(VisualizationAction.OnStopClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.outline_stop_circle_24),
                            contentDescription = "Add"
                        )
                    }

                    OutlinedIconButton (
                        onClick = {
                            onAction(VisualizationAction.OnGifClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.outline_gif_24),
                            contentDescription = "Add"
                        )
                    }

                    OutlinedIconButton (
                        onClick = {
                            onAction(VisualizationAction.OnPlayBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.outline_skip_previous_24),
                            contentDescription = "Add"
                        )
                    }

                    OutlinedIconButton (
                        onClick = {
                            onAction(VisualizationAction.OnPlayForwardClicked)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector
                                .vectorResource(id = R.drawable.outline_skip_next_24),
                            contentDescription = "Add"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SortingVisualizer(
                modifier = Modifier
                    .weight(1f),
                elements = state.uiList,
                selected = state.selected,
                comparedTo = state.comparedTo
            )

            Box(
                modifier = Modifier
                    //.weight(1f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(150.dp),
                    progress = { state.progress },
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )

                Text(
                    text = "${state.progress.toString().format(".2f")} %",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }

            SliderWithLabel(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.choose_the_frequency_aka_execution_speed),
                sliderValue = state.frequency,
                onSliderValueChanged = {
                    onAction(VisualizationAction.OnFrequencyChanged(it))
                }
            )
        }

    }
}