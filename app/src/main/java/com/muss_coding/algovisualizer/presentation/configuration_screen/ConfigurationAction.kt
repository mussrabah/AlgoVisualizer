package com.muss_coding.algovisualizer.presentation.configuration_screen

sealed interface ConfigurationAction {
    data class ChooseAnAlgorithmTextFieldValueChanged(val query: String): ConfigurationAction

    data object Expanded: ConfigurationAction

    data class OnFrequencyChanged(val frequency: Int): ConfigurationAction


    data class OnDataSizeChanged(val dataSize: Int): ConfigurationAction

    data class OnSortingAlgorithmChanged(val sortingAlgorithm: String): ConfigurationAction
}