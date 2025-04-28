package com.muss_coding.algovisualizer.presentation.visualization_screen

import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms

sealed interface VisualizationAction {
    data class OnFrequencyChanged(val frequency: Int) : VisualizationAction
    data class OnSizeUpdated(val size: Int) : VisualizationAction
    data object OnPlayClicked: VisualizationAction
    data object OnStopClicked: VisualizationAction
    data object OnPlayBackClicked: VisualizationAction
    data object OnPlayForwardClicked: VisualizationAction
    data object OnGifClicked: VisualizationAction
    data class OnAlgorithmChanged(val algorithm: SortingAlgorithms) : VisualizationAction
}