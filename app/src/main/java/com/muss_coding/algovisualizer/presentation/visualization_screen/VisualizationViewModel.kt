package com.muss_coding.algovisualizer.presentation.visualization_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import com.muss_coding.algovisualizer.domain.use_case.SortingUseCases
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationEvent
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

class VisualizationViewModel(
    private val sortingUseCases: SortingUseCases
): ViewModel() {

    private val _state = MutableStateFlow(VisualizationState())
    val state = _state
        .onStart {
            generateData()
            Log.d("VisualizationViewModel", "generating data with size ${_state.value.size}")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            VisualizationState()
        )


    private val _events = Channel<ConfigurationEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: VisualizationAction) {
        when (action) {
            is VisualizationAction.OnFrequencyChanged -> {
                _state.update {
                    it.copy(
                        frequency = action.frequency
                    )
                }
                Log.d("VisualizationViewModel", "frequency changed to ${_state.value.frequency}")
            }
            is VisualizationAction.OnGifClicked -> {}
            is VisualizationAction.OnPlayBackClicked -> {

            }
            is VisualizationAction.OnPlayClicked -> {
                launchSorting()
            }
            is VisualizationAction.OnPlayForwardClicked -> {}
            is VisualizationAction.OnStopClicked -> {}
            is VisualizationAction.OnSizeUpdated -> {
                _state.update {
                    it.copy(
                        size = action.size
                    )
                }
            }

            is VisualizationAction.OnAlgorithmChanged -> {
                _state.update {
                    it.copy(
                        algorithm = action.algorithm
                    )
                }
            }
        }
    }

    private fun launchSorting() {
        //viewModelScope.launch {
            when(_state.value.algorithm) {
                SortingAlgorithms.BUBBLE_SORT -> sortingUseCases.bubbleSort(_state.value.uiList)
                SortingAlgorithms.SELECTION_SORT -> sortingUseCases.selectionSort(_state.value.uiList)
                SortingAlgorithms.INSERTION_SORT -> sortingUseCases.insertionSort(_state.value.uiList)
                SortingAlgorithms.MERGE_SORT -> sortingUseCases.mergeSort(_state.value.uiList)
                SortingAlgorithms.QUICK_SORT -> sortingUseCases.quickSort(_state.value.uiList)
            }.onEach { sortStep ->
                _state.update { state ->
                    state.copy(
                        uiList = sortStep.listState,
                        selected = sortStep.highlightedIndices.first,
                        comparedTo = sortStep.highlightedIndices.second,
                        progress = formatProgress(sortStep.progress).toFloat()
                    )
                }
                delay(state.value.frequency.toLong().times(10))
            }.launchIn(viewModelScope)

    }

    private fun generateData() {
        viewModelScope.launch {
            val randomValues = List(_state.value.size) {
                Random.nextInt(10, 100)
            }
            _state.update {
                it.copy(
                    uiList = randomValues
                )
            }

            Log.d("VisualizationViewModel", "generated data ${_state.value.uiList}")

        }
    }

    private fun formatProgress(progress: Float): String {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.DOWN
        return decimalFormat.format(progress)
    }
}