package com.muss_coding.algovisualizer.domain.use_case

import com.muss_coding.algovisualizer.domain.model.SortStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuickSortUseCase{
    operator fun invoke(list: List<Int>): Flow<SortStep> = flow {
        val sortableList = list.toMutableList()
        val n = sortableList.size
        var comparisons = 0
        val totalComparisons = n * (n - 1) / 2

        suspend fun partition(low: Int, high: Int): Int {
            val pivot = sortableList[high]
            var i = low - 1
            for (j in low until high) {
                comparisons++
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(j, high),
                        isSwap = false,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
                if (sortableList[j] < pivot) {
                    i++
                    val temp = sortableList[i]
                    sortableList[i] = sortableList[j]
                    sortableList[j] = temp
                    emit(
                        SortStep(
                            listState = sortableList.toList(),
                            highlightedIndices = Pair(i, j),
                            isSwap = true,
                            progress = (comparisons.toFloat() / totalComparisons) * 100
                        )
                    )
                }
            }
            val temp = sortableList[i + 1]
            sortableList[i + 1] = sortableList[high]
            sortableList[high] = temp
            emit(
                SortStep(
                    listState = sortableList.toList(),
                    highlightedIndices = Pair(i + 1, high),
                    isSwap = true,
                    progress = (comparisons.toFloat() / totalComparisons) * 100
                )
            )
            return i + 1
        }

        suspend fun quickSort(low: Int, high: Int) {
            if (low < high) {
                val pi = partition(low, high)
                quickSort(low, pi - 1)
                quickSort(pi + 1, high)
            }
        }

        quickSort(0, n - 1)
    }
}