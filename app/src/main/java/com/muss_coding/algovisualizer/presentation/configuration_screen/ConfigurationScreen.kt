package com.muss_coding.algovisualizer.presentation.configuration_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.List
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muss_coding.algovisualizer.R
import com.muss_coding.algovisualizer.presentation.configuration_screen.components.ChooseAnAlgorithmDropDownMenu
import com.muss_coding.algovisualizer.presentation.configuration_screen.components.SliderWithLabel
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(
    modifier: Modifier = Modifier,
    state: ConfigurationState,
    onAction: (ConfigurationAction) -> Unit,
    onEvent: (ConfigurationEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Sharp.List,
                        contentDescription = null
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Sharp.AccountCircle,
                        contentDescription = null
                    )
                },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.configuration),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
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
                    }
                )

            }

            OutlinedButton(
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                onClick = {
                    onEvent(ConfigurationEvent.OnStartClicked)
                },
                modifier = Modifier.fillMaxWidth().height(60.dp)
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