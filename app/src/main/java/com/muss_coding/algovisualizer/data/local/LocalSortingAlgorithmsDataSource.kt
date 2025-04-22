package com.muss_coding.algovisualizer.data.local

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.muss_coding.algovisualizer.domain.data_source.SortingAlgorithmsDataSource
import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms

class LocalSortingAlgorithmsDataSource: SortingAlgorithmsDataSource {

    override suspend fun getSortingAlgorithms(): List<String> {
        return SortingAlgorithms.entries.map {
            it.name.replace(oldChar = '_', newChar = ' ').lowercase().capitalize(Locale.current)
        }
    }
}