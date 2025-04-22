package com.muss_coding.algovisualizer.presentation.configuration_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muss_coding.algovisualizer.domain.data_source.SortingAlgorithmsDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigurationViewModel(
    private val sortingAlgorithmsDatasource: SortingAlgorithmsDataSource
): ViewModel() {

    private val _state = MutableStateFlow(ConfigurationState())
    val state = _state
        .onStart { loadSortingAlgorithms() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ConfigurationState()
        )


    private val _events = Channel<ConfigurationEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ConfigurationAction) {
        when(action) {
            is ConfigurationAction.ChooseAnAlgorithmTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        selectedAlgorithm = action.query
                    )
                }
            }
            is ConfigurationAction.Expanded -> {
                _state.update {
                    it.copy(
                        expanded = !it.expanded
                    )
                }
            }
            is ConfigurationAction.OnDataSizeChanged -> {
                _state.update {
                    it.copy(
                        dataSize = action.dataSize
                    )
                }
            }
            is ConfigurationAction.OnFrequencyChanged -> {
                _state.update {
                    it.copy(
                        frequency = action.frequency
                    )
                }
            }
            is ConfigurationAction.OnSortingAlgorithmChanged -> {
                _state.update {
                    it.copy(
                        selectedAlgorithm = action.sortingAlgorithm
                    )
                }
            }
        }
    }

    private fun loadSortingAlgorithms() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    sortingAlgorithms = sortingAlgorithmsDatasource.getSortingAlgorithms()
                )
            }
        }
    }
}