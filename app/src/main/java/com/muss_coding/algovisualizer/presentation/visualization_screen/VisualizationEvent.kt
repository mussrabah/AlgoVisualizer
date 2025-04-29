package com.muss_coding.algovisualizer.presentation.visualization_screen

import com.muss_coding.algovisualizer.domain.model.SortStep

interface VisualizationEvent {
    data class OnCaptureBitmap(val sortStep: SortStep): VisualizationEvent
}