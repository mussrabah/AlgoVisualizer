package com.muss_coding.algovisualizer.presentation.configuration_screen

import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationUI(
    val algorithm: SortingAlgorithms,
    val frequency: Int,
    val size: Int
)
