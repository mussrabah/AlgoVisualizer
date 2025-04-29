package com.muss_coding.algovisualizer.presentation.visualization_screen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muss_coding.algovisualizer.domain.model.SortStep
import com.muss_coding.algovisualizer.domain.model.SortingAlgorithms
import com.muss_coding.algovisualizer.domain.use_case.SortingUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

class VisualizationViewModel(
    private val sortingUseCases: SortingUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(VisualizationState())
    val state = _state
        .onStart {
            generateData()
            Log.d("VisualizationViewModel", "generating data with size ${_state.value.size}")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            VisualizationState()
        )


    private val _events = Channel<VisualizationEvent>()
    val events = _events.receiveAsFlow()

    private var job = SupervisorJob()

    private var scope = CoroutineScope(viewModelScope.coroutineContext + job)

    private val pauseMutex = Mutex(locked = true)

    private var backgroundJob: Job? = null

    private val capturedBitmaps = mutableListOf<Bitmap>()

    private var sortingBacktrack = mutableListOf<SortStep>()


    fun onAction(action: VisualizationAction) {
        when (action) {
            is VisualizationAction.OnFrequencyChanged -> {
                _state.update {
                    it.copy(
                        frequency = action.frequency
                    )
                }
                Log.d("VisualizationViewModel", "frequency changed to ${_state.value.frequency}")
            }
            is VisualizationAction.OnGifClicked -> {
                viewModelScope.launch {
                    sortingBacktrack.forEach {
                        _events.send(VisualizationEvent.OnCaptureBitmap(it))
                        delay(100)
                    }

                    val savedImageUris = BitmapUtils.compileBitmapsToImages(
                        context = action.context,
                        bitmaps = capturedBitmaps,
                        baseFilename = "processed_image",
                        format = Bitmap.CompressFormat.PNG, // Or JPEG
                        quality = 100 // Adjust quality as needed
                    )

                    if (savedImageUris.isNotEmpty()) {
                        Log.d("MyApp", "Successfully saved ${savedImageUris.size} images. Uris: $savedImageUris")
                        // You can now work with the saved image Uris (e.g., share them)
                    } else {
                        Log.e("MyApp", "Failed to save one or more bitmaps.")
                    }

                }


/*                generateGifFromBitmaps(
                    bitmaps = capturedBitmaps,
                    outputFile = action.file,
                    delayMs = 10
                )*/
            }
            is VisualizationAction.OnPlayBackClicked -> {

            }
            is VisualizationAction.OnPlayClicked -> {
                if (_state.value.play) {
                    resumeTask()
                    if (_state.value.beforeSortingList.isEmpty()) {
                        _state.update {
                            it.copy(
                                beforeSortingList = _state.value.uiList
                            )
                        }
                    }
                    launchSorting()
                } else {
                    pauseTask()
                }
            }
            is VisualizationAction.OnPlayForwardClicked -> {}
            is VisualizationAction.OnStopClicked -> {
                cancelTask()
                _state.update {
                    it.copy(
                        play = true,
                        selected = 0,
                        comparedTo = 0,
                        progress = 0f,
                        uiList = _state.value.beforeSortingList,
                        beforeSortingList = emptyList()
                    )
                }
            }
            is VisualizationAction.OnSizeUpdated -> {
                _state.update {
                    it.copy(
                        size = action.size
                    )
                }
            }

            is VisualizationAction.OnAlgorithmChanged -> {
                _state.update {
                    it.copy(
                        algorithm = action.algorithm
                    )
                }
            }

            is VisualizationAction.OnSaveBitmap -> {
                capturedBitmaps.add(action.bitmap)
            }
        }
    }

    private fun launchSorting() {
        if (backgroundJob?.isActive == true) return
        //sortingBacktrack = mutableListOf()

        backgroundJob = scope.launch {
            when(_state.value.algorithm) {
                SortingAlgorithms.BUBBLE_SORT -> sortingUseCases.bubbleSort(_state.value.uiList)
                SortingAlgorithms.SELECTION_SORT -> sortingUseCases.selectionSort(_state.value.uiList)
                SortingAlgorithms.INSERTION_SORT -> sortingUseCases.insertionSort(_state.value.uiList)
                SortingAlgorithms.MERGE_SORT -> sortingUseCases.mergeSort(_state.value.uiList)
                SortingAlgorithms.QUICK_SORT -> sortingUseCases.quickSort(_state.value.uiList)
            }.onEach { sortStep ->
                sortingBacktrack.add(sortStep)
            }.onCompletion {
                val listIterator = sortingBacktrack.iterator()
                while (listIterator.hasNext()) {
                    pauseMutex.lock()
                    pauseMutex.unlock()
                    //work
                    val sortStep = listIterator.next()
                    _state.update { state ->
                        state.copy(
                            uiList = sortStep.listState,
                            selected = sortStep.highlightedIndices.first,
                            comparedTo = sortStep.highlightedIndices.second,
                            progress = formatProgress(sortStep.progress).toFloat()
                        )
                    }
                    //_events.send(VisualizationEvent.OnCaptureBitmap)
                    delay(state.value.frequency.toLong().times(10))

                    //check if job cancelled
                    if (!isActive) {
                        Log.d("VisualizationViewModel", "job cancelled")
                        break
                    }

                }
            }.collect {
                Log.d("VisualizationViewModel", "progress: ${_state.value.progress}")
            }
        }
    }

    private fun pauseTask() {
        if (!_state.value.play) {
            _state.update {
                it.copy(
                    play = true
                )
            }
            scope.launch {
                pauseMutex.lock()
            }
            println("Paused")
        }
    }

    private fun resumeTask() {
        if (_state.value.play) {
            _state.update {
                it.copy(
                    play = false
                )
            }
            pauseMutex.unlock()
            println("Resumed")
        }
    }

    private fun cancelTask() {
        backgroundJob?.cancel()
        if (!pauseMutex.isLocked) {
            scope.launch {
                pauseMutex.lock()
            }
        }
        sortingBacktrack.clear()
        println("Cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel() // cancel everything on ViewModel destroy
    }

    private fun generateData() {
        viewModelScope.launch {
            val randomValues = List(_state.value.size) {
                Random.nextInt(10, 100)
            }
            _state.update {
                it.copy(
                    uiList = randomValues
                )
            }

            Log.d("VisualizationViewModel", "generated data ${_state.value.uiList}")

        }
    }

    private fun formatProgress(progress: Float): String {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.DOWN
        return decimalFormat.format(progress)
    }
}