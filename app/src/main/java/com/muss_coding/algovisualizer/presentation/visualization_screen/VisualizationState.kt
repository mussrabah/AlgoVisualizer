package com.muss_coding.algovisualizer.presentation.visualization_screen

import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms

data class VisualizationState(
    val selected: Int = 0,
    val comparedTo: Int = 0,
    val progress: Float = 0f,
    val frequency: Int = 0,
    val size: Int = 0,
    val uiList: List<Int> = emptyList(),
    val algorithm: SortingAlgorithms = SortingAlgorithms.BUBBLE_SORT
)
