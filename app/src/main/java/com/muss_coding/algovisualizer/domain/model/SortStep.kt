package com.muss_coding.algovisualizer.domain.model

data class SortStep(
    val listState: List<Int>,
    val highlightedIndices: Pair<Int, Int>,
    val isSwap: Boolean
)
