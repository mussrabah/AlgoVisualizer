package com.muss_coding.algovisualizer

import android.app.Application
import com.muss_coding.algovisualizer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AlgoVisualizerApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AlgoVisualizerApp)
            androidLogger()


            modules(appModule)
        }
    }
}