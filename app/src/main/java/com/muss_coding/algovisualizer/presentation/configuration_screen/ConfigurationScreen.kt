package com.muss_coding.algovisualizer.presentation.configuration_screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muss_coding.algovisualizer.R
import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import com.muss_coding.algovisualizer.presentation.configuration_screen.components.ChooseAnAlgorithmDropDownMenu
import com.muss_coding.algovisualizer.presentation.configuration_screen.components.SliderWithLabel
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme

@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier,
    state: ConfigurationState,
    onAction: (ConfigurationAction) -> Unit,
    onEvent: (ConfigurationEvent) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ChooseAnAlgorithmDropDownMenu(
                expanded = state.expanded,
                chooseAnAlgorithmTextFieldValue = state.selectedAlgorithm,
                sortingAlgorithms = state.sortingAlgorithms,
                onExpandedChanged = {
                    onAction(ConfigurationAction.Expanded)
                },
                onQueryChanged = {
                    onAction(ConfigurationAction.ChooseAnAlgorithmTextFieldValueChanged(it))
                },
                onItemClicked = {
                    onAction(ConfigurationAction.OnSortingAlgorithmChanged(it))
                }
            )

            SliderWithLabel(
                text = stringResource(R.string.choose_the_frequency_aka_execution_speed),
                sliderValue = state.frequency,
                onSliderValueChanged = {
                    onAction(ConfigurationAction.OnFrequencyChanged(it))
                }
            )

            SliderWithLabel(
                text = stringResource(R.string.choose_size_of_data_from_6_to_15),
                sliderValue = state.dataSize,
                onSliderValueChanged = {
                    onAction(ConfigurationAction.OnDataSizeChanged(it))
                },
                valueRange = 6f..15f,
                steps = 15 - 6 + 1 - 2
            )

        }

        OutlinedButton(
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
            onClick = {
                val isThereChosenAlgorithm = state.selectedAlgorithm.isNotEmpty()
                val isThereChosenFrequency = state.frequency != 0
                val isThereChosenSize = state.dataSize != 0

                println("algorithm: $isThereChosenAlgorithm , frequency: $isThereChosenFrequency , size: $isThereChosenSize")

                if (!isThereChosenSize || !isThereChosenFrequency ||  !isThereChosenAlgorithm) {
                    Toast.makeText(
                        context,
                        "Please choose an algorithm, frequency and size",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onEvent(
                        ConfigurationEvent
                            .OnStartClicked(
                                ConfigurationUI(
                                    algorithm = SortingAlgorithms.entries.find {
                                        it
                                            .name
                                            .replace(oldChar = '_', newChar = ' ')
                                            .lowercase()
                                            .capitalize(Locale.current) == state.selectedAlgorithm
                                    }!!,
                        frequency = state.frequency,
                        size = state.dataSize
                    )))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(R.string.start),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp,
                fontSize = 20.sp
            )
        }
    }
}


@PreviewLightDark
@Composable
fun ConfigurationScreenPreview(modifier: Modifier = Modifier) {
    AlgoVisualizerTheme {
        ConfigurationScreen(
            modifier = modifier,
            state = ConfigurationState(),
            onAction = {},
            onEvent = {}
        )
    }
}