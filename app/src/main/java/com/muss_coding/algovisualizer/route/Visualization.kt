package com.muss_coding.algovisualizer.route

import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationUI
import kotlinx.serialization.Serializable

@Serializable
data class Visualization(
    val algorithm: SortingAlgorithms,
    val frequency: Int,
    val size: Int
)