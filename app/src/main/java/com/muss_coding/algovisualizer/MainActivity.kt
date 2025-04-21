package com.muss_coding.algovisualizer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muss_coding.algovisualizer.presentation.onboarding.OnBoardingEvents
import com.muss_coding.algovisualizer.presentation.onboarding.OnBoardingScreen
import com.muss_coding.algovisualizer.presentation.onboarding.OnBoardingViewModel
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgoVisualizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = viewModels<OnBoardingViewModel>().value
                    val state = viewModel.state.collectAsStateWithLifecycle()
                    OnBoardingScreen(
                        modifier = Modifier.padding(innerPadding),
                        state = state.value,
                        onAction = { action ->
                            when(action) {
                                is OnBoardingEvents.OnStart -> {
                                    Toast.makeText(this, "Start", Toast.LENGTH_SHORT)
                                        .show()
                                    viewModel.onAction(action)
                                }
                                else -> {
                                    viewModel.onAction(action)
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlgoVisualizerTheme {
        Greeting("Android")
    }
}