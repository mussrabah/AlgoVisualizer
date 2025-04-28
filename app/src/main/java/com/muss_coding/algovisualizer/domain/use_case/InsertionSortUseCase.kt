package com.muss_coding.algovisualizer.domain.use_case

import com.muss_coding.algovisualizer.domain.model.SortStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertionSortUseCase {
    operator fun invoke(list: List<Int>): Flow<SortStep> = flow {
        val sortableList = list.toMutableList()
        val n = sortableList.size
        var comparisons = 0
        val totalComparisons = n * (n - 1) / 2

        for (i in 1 until n) {
            val key = sortableList[i]
            var j = i - 1

            while (j >= 0 && sortableList[j] > key) {
                comparisons++
                sortableList[j + 1] = sortableList[j]
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(j, j + 1),
                        isSwap = true,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
                j--
            }
            sortableList[j + 1] = key
            // One comparison happens even if while condition fails immediately
            if (j >= 0) {
                comparisons++
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(j, i),
                        isSwap = false,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
            }
        }
    }
}