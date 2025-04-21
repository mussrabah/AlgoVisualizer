package com.muss_coding.algovisualizer.presentation.onboarding

data class OnBoardingState(
    val iteration: Int = 0,
    val selectedOption: Int = 0,
    val shouldSowBars: Boolean = false,
    val bars: List<ArrayModel> = emptyList(),
    val selectedBar: ArrayModel? = null,
    val comparedToBar: ArrayModel? = null,
)
