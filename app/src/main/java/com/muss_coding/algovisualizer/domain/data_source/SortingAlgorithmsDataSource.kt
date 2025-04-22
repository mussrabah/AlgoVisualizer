package com.muss_coding.algovisualizer.domain.data_source

interface SortingAlgorithmsDataSource {
    suspend fun getSortingAlgorithms(): List<String>
}