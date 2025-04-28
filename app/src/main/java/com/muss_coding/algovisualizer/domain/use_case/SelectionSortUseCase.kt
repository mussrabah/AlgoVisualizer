package com.muss_coding.algovisualizer.domain.use_case

import com.muss_coding.algovisualizer.domain.model.SortStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectionSortUseCase {
    operator fun invoke(list: List<Int>): Flow<SortStep> = flow {
        val sortableList = list.toMutableList()
        val n = sortableList.size
        var comparisons = 0
        val totalComparisons = n * (n - 1) / 2

        for (i in 0 until n - 1) {
            var minIdx = i
            for (j in i + 1 until n) {
                comparisons++
                if (sortableList[j] < sortableList[minIdx]) {
                    minIdx = j
                }
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(i, j),
                        isSwap = false,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
            }
            if (minIdx != i) {
                val temp = sortableList[i]
                sortableList[i] = sortableList[minIdx]
                sortableList[minIdx] = temp
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(i, minIdx),
                        isSwap = true,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
            }
        }
    }
}