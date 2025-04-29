package com.muss_coding.algovisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.List
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationEvent
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationScreen
import com.muss_coding.algovisualizer.presentation.configuration_screen.ConfigurationViewModel
import com.muss_coding.algovisualizer.presentation.util.ObserveAsEvents
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationAction
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationEvent
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationScreen
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationViewModel
import com.muss_coding.algovisualizer.presentation.util.renderComposableToBitmap
import com.muss_coding.algovisualizer.presentation.util.renderComposableToBitmapSafely
import com.muss_coding.algovisualizer.route.Configuration
import com.muss_coding.algovisualizer.route.Visualization
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgoVisualizerTheme {
                var topAppBarTitle by remember {
                    mutableIntStateOf(R.string.configuration)
                }

                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    modifier = Modifier.fillMaxSize(),
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.fillMaxWidth(.7f)
                        ) {
                            Text(
                                text = "Drawer"
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Sharp.List,
                                            contentDescription = null
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(
                                        onClick = {

                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Sharp.AccountCircle,
                                            contentDescription = null
                                        )
                                    }
                                },
                                title = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(topAppBarTitle),
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        val navController = rememberNavController()
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = Configuration
                        ) {
                            composable<Configuration> {
                                topAppBarTitle = R.string.configuration
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
                                                navController.navigate(Visualization(
                                                    algorithm = event.configurationUI.algorithm,
                                                    frequency = event.configurationUI.frequency,
                                                    size = event.configurationUI.size,
                                                ))
                                            }
                                        }
                                    }
                                )
                            }

                            composable<Visualization> {
                                val visualization = it.toRoute<Visualization>()
                                topAppBarTitle = R.string.visualization

                                val viewModel = koinViewModel<VisualizationViewModel>()
                                val state = viewModel.state.collectAsStateWithLifecycle()
                                if (state.value.size == 0 || state.value.frequency == 0) {
                                    viewModel
                                        .onAction(
                                            VisualizationAction
                                                .OnFrequencyChanged(visualization.frequency)
                                        )
                                    viewModel
                                        .onAction(
                                            VisualizationAction
                                                .OnSizeUpdated(visualization.size)
                                        )
                                    viewModel
                                        .onAction(
                                            VisualizationAction
                                                .OnAlgorithmChanged(visualization.algorithm)
                                        )
                                }

                                VisualizationScreen(
                                    state = state.value,
                                    onAction = { action ->
                                        viewModel.onAction(action)
                                    }
                                )

                                //get device width
                                val deviceWidth = resources.displayMetrics.widthPixels

                                ObserveAsEvents(events = viewModel.events) { event ->
                                    when(event) {
                                        is VisualizationEvent.OnCaptureBitmap -> {
                                            coroutineScope.launch { 
//                                                val bitmap = renderComposableToBitmap(
//                                                    context = this@MainActivity,
//                                                    state = state.value,
//                                                    width = deviceWidth.toInt(),
//                                                    height = 200
//                                                )
//
//                                                viewModel.onAction(VisualizationAction.OnSaveBitmap(bitmap))
                                                renderComposableToBitmapSafely(
                                                    activity = this@MainActivity,
                                                    sortStep = event.sortStep,
                                                    widthPx = deviceWidth,
                                                    heightPx = 200 * resources.displayMetrics.density.toInt(),
                                                    onBitmapReady = { bitmap ->
                                                        viewModel.onAction(VisualizationAction.OnSaveBitmap(bitmap))
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}