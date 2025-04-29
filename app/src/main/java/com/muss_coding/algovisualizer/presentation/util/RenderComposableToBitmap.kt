package com.muss_coding.algovisualizer.presentation.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.muss_coding.algovisualizer.domain.model.SortStep
import com.muss_coding.algovisualizer.presentation.visualization_screen.VisualizationState
import com.muss_coding.algovisualizer.presentation.visualization_screen.components.SortingVisualizer
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme

fun renderComposableToBitmap(context: Context, state: VisualizationState, width: Int, height: Int): Bitmap {
    val composeView = ComposeView(context)
    composeView.setContent {
        SortingVisualizer(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elements = state.uiList,
            selected = state.selected,
            comparedTo = state.comparedTo
        )
    }

    val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    composeView.measure(widthSpec, heightSpec)
    composeView.layout(0, 0, width, height)

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    composeView.draw(canvas)
    return bitmap
}

fun renderComposableToBitmapSafely(
    activity: Activity,
    sortStep: SortStep,
    widthPx: Int,
    heightPx: Int,
    onBitmapReady: (Bitmap) -> Unit
) {
    val composeView = ComposeView(activity)
    composeView.setContent {
        AlgoVisualizerTheme {
            SortingVisualizer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elements = sortStep.listState,
                selected = sortStep.highlightedIndices.first,
                comparedTo = sortStep.highlightedIndices.second
            )
        }
    }

    val container = FrameLayout(activity).apply {
        visibility = View.INVISIBLE
        layoutParams = ViewGroup.LayoutParams(widthPx, heightPx)
    }

    container.addView(composeView)
    val rootView = activity.window.decorView as ViewGroup
    rootView.addView(container)

    composeView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            composeView.viewTreeObserver.removeOnPreDrawListener(this)

            composeView.measure(
                View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY)
            )
            composeView.layout(0, 0, widthPx, heightPx)

            val bitmap = createBitmap(widthPx, heightPx)
            val canvas = Canvas(bitmap)
            composeView.draw(canvas)

            rootView.removeView(container)
            onBitmapReady(bitmap)

            return true
        }
    })
}
