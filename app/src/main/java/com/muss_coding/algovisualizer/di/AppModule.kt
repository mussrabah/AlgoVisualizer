package com.muss_coding.algovisualizer.di

import com.muss_coding.algovisualizer.data.local.LocalSortingAlgorithmsDataSource
import com.muss_coding.algovisualizer.domain.data_source.SortingAlgorithmsDataSource
import com.muss_coding.algovisualizer.domain.use_case.SortingUseCases
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationViewModel
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::SortingUseCases)
    viewModelOf(::VisualizationViewModel)

    singleOf(::LocalSortingAlgorithmsDataSource).bind<SortingAlgorithmsDataSource>()

    viewModelOf(::ConfigurationViewModel)
}