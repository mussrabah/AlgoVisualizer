package com.muss_coding.algovisualizer.domain.use_case

import com.muss_coding.algovisualizer.domain.model.SortStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MergeSortUseCase {
    operator fun invoke(list: List<Int>): Flow<SortStep> = flow {
        val sortableList = list.toMutableList()
        val n = sortableList.size
        var comparisons = 0
        val totalComparisons = n * (n - 1) / 2

        suspend fun merge(l: Int, m: Int, r: Int) {
            val left = sortableList.subList(l, m + 1).toMutableList()
            val right = sortableList.subList(m + 1, r + 1).toMutableList()

            var i = 0
            var j = 0
            var k = l

            while (i < left.size && j < right.size) {
                comparisons++
                emit(
                    SortStep(
                        listState = sortableList.toList(),
                        highlightedIndices = Pair(l + i, m + 1 + j),
                        isSwap = false,
                        progress = (comparisons.toFloat() / totalComparisons) * 100
                    )
                )
                if (left[i] <= right[j]) {
                    sortableList[k] = left[i]
                    i++
                } else {
                    sortableList[k] = right[j]
                    j++
                }
                k++
            }
            while (i < left.size) {
                sortableList[k] = left[i]
                i++
                k++
            }
            while (j < right.size) {
                sortableList[k] = right[j]
                j++
                k++
            }
        }

        suspend fun mergeSort(l: Int, r: Int) {
            if (l < r) {
                val m = (l + r) / 2
                mergeSort(l, m)
                mergeSort(m + 1, r)
                merge(l, m, r)
            }
        }

        mergeSort(0, n - 1)
    }
}