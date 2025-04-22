package com.muss_coding.algovisualizer.presentation.visualization_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VisualizationScreen(modifier: Modifier = Modifier, name: String) {
    Text(text = "Visualization Screen $name")
}