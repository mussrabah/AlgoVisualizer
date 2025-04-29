package com.muss_coding.algovisualizer.presentation.configuration_screen

data class ConfigurationState(
    val sortingAlgorithms: List<String> = emptyList(),
    val selectedAlgorithm: String = "",
    val frequency: Int = 50,
    val dataSize: Int = 0,
    val expanded: Boolean = false,
)