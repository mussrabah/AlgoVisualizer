package com.muss_coding.algovisualizer.presentation.visualization_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muss_coding.algovisualizer.ui.theme.AlgoVisualizerTheme

@Composable
fun SortingVisualizer(
    modifier: Modifier = Modifier,
    elements: List<Int>,
    selected: Int = 2,
    comparedTo: Int = 8
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val displayMetrics = LocalContext.current.resources.displayMetrics
            val width = displayMetrics.widthPixels / displayMetrics.density
            val widthPerEach = (width / (elements.size + 8*elements.size)).dp

            elements.forEachIndexed {index, element ->
                val color = when (index) {
                    selected -> Color.Green
                    comparedTo -> Color.Yellow
                    else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                }
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                            ))
                        .weight(1f)
                        .fillMaxHeight(element / 100f)
                        .width(widthPerEach)
                        .background(color)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(Color.Green)
                )

                Text(
                    text = "Selected element",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(Color.Yellow)
                )

                Text(
                    text = "Comparing with Selected",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
fun SortingVisualizerPreview() {
    AlgoVisualizerTheme {
        SortingVisualizer(
            elements = emptyList()
        )
    }
}