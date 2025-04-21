package com.muss_coding.algovisualizer.presentation.onboarding

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class OnBoardingViewModel: ViewModel() {

    private val _state = MutableStateFlow(OnBoardingState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            OnBoardingState()
        )


    fun onAction(event: OnBoardingEvents) {
        when(event) {
            is OnBoardingEvents.OnIterationChanged -> {
                _state.update {
                    it.copy(iteration = event.iteration)
                }
            }
            is OnBoardingEvents.OnOptionSelected -> {
                _state.update {
                    it.copy(selectedOption = event.selected)
                }
            }
            is OnBoardingEvents.OnStart -> {
                _state.update {
                    it.copy(shouldSowBars = !_state.value.shouldSowBars)
                }

                viewModelScope.launch {
                    val modelArray = (1.._state.value.iteration).map {
                        Random.nextInt(10, 50)
                    }
                    val models = modelArray.map {
                        ArrayModel(
                            name = "$it",
                            value = it,
                            color = Color(
                                Random.nextInt(256),
                                Random.nextInt(256),
                                Random.nextInt(256)
                            )
                        )
                    }

                    _state.update {
                        it.copy(bars = models)
                    }
                }

                selectionSort()


            }
        }
    }

    private fun selectionSort() {
        viewModelScope.launch {
            val arr = _state.value.bars.toMutableList()
            val n = arr.size

            for (i in 0 until n - 1) {
                var minIndex = i
                _state.update {
                    it.copy(
                        selectedBar = arr[i]
                    )
                }
                for (j in i + 1 until n) {
                    _state.update {
                        it.copy(
                            comparedToBar = arr[j]
                        )
                    }
                    if (arr[j].value < arr[minIndex].value) {
                        minIndex = j
                    }
                    delay(1000L)
                }

                _state.update {
                    it.copy(
                        comparedToBar = null
                    )
                }

                // Swap the found minimum element with the first element
                val temp = arr[minIndex]
                arr[minIndex] = arr[i]
                arr[i] = temp

                _state.update {
                    it.copy(bars = arr.toList())
                }
                Log.d("VM", "${arr.map { it.value }}")
                delay(2000L)
            }
            _state.update {
                it.copy(
                    selectedBar = null,
                    comparedToBar = null,
                    shouldSowBars = false,
                    bars = emptyList(),
                    selectedOption = 0,
                    iteration = 0
                )
            }
        }
    }

}