package com.muss_coding.algovisualizer.presentation.configuration_screen

interface ConfigurationEvent {
    data class OnStartClicked(val configurationUI: ConfigurationUI): ConfigurationEvent
}