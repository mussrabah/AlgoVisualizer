package com.muss_coding.algovisualizer.domain.use_case

import com.muss_coding.algovisualizer.domain.model.SortStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSortUseCase {
    operator fun invoke(list: List<Int>): Flow<SortStep> = flow {
        val sortableList = list.toMutableList()
        val n = sortableList.size
        var comparisons = 0
        val totalComparisons = n * (n - 1) / 2

        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                val isSwap = sortableList[j] > sortableList[j + 1]
                comparisons++
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(i, j),
                        isSwap = isSwap,
                        progress = (comparisons.toFloat() / totalComparisons)*100
                    )
                )
                if (isSwap) {
                    // Swap the elements
                    val temp = sortableList[j]
                    sortableList[j] = sortableList[j + 1]
                    sortableList[j + 1] = temp
                }
            }
        }
    }
}