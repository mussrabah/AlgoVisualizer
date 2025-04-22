package com.muss_coding.algovisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationEvent
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationScreen
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationViewModel
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationScreen
import com.muss_coding.algovisualizer.route.Configuration
import com.muss_coding.algovisualizer.route.Visualization
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgoVisualizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*val viewModel = viewModels<OnBoardingViewModel>().value
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
                    )*/

                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Configuration
                    ) {
                        composable<Configuration> {
                            val viewModel: ConfigurationViewModel = koinViewModel()
                            val state = viewModel.state.collectAsStateWithLifecycle()
                            ConfigurationScreen(
                                state = state.value,
                                onAction = { action ->
                                    viewModel.onAction(action)
                                },
                                onEvent = { event ->
                                    when(event) {
                                        is ConfigurationEvent.OnStartClicked -> {
                                            navController.navigate(Visualization(name = state.value.selectedAlgorithm))
                                        }
                                    }
                                }
                            )
                        }

                        composable<Visualization> {
                            val visualization = it.toRoute<Visualization>()

                            VisualizationScreen(
                                name = visualization.name
                            )
                        }
                    }
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