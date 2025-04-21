package com.muss_coding.algovisualizer.presentation.onboarding

sealed interface OnBoardingEvents {
    data class OnIterationChanged(val iteration: Int): OnBoardingEvents
    data class OnOptionSelected(val selected: Int): OnBoardingEvents
    data object OnStart: OnBoardingEvents

}