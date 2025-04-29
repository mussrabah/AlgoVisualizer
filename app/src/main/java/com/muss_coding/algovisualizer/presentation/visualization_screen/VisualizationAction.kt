package com.muss_coding.algovisualizer.presentation.visualization_screen

import android.content.Context
import android.graphics.Bitmap
import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import java.io.File

sealed interface VisualizationAction {
    data class OnFrequencyChanged(val frequency: Int) : VisualizationAction
    data class OnSizeUpdated(val size: Int) : VisualizationAction
    data object OnPlayClicked: VisualizationAction
    data object OnStopClicked: VisualizationAction
    data object OnPlayBackClicked: VisualizationAction
    data object OnPlayForwardClicked: VisualizationAction
    data class OnGifClicked(val file: File, val context: Context): VisualizationAction
    data class OnAlgorithmChanged(val algorithm: SortingAlgorithms) : VisualizationAction
    data class OnSaveBitmap(val bitmap: Bitmap) : VisualizationAction
}