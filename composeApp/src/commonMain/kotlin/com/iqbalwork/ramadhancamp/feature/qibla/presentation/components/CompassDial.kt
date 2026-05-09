package com.iqbalwork.ramadhancamp.feature.qibla.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassDial(
    heading: Float,
    bearingToKaaba: Float?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        color = colors.textPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2

            rotate(degrees = -heading, pivot = center) {
                val petalColor = Color(0xFF132F28).copy(alpha = 0.35f)
                val petalSize = Size(radius * 1.8f, radius * 1.8f)
                val petalOffset = Offset(center.x - petalSize.width / 2, center.y - petalSize.height / 2)
                for (i in 0 until 4) {
                    rotate(degrees = i * 45f, pivot = center) {
                        drawRoundRect(
                            color = petalColor,
                            topLeft = petalOffset,
                            size = petalSize,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius * 0.5f)
                        )
                    }
                }

                drawCircle(
                    color = colors.bgSecondary,
                    radius = radius,
                    center = center
                )

                for (i in 0 until 72) {
                    val angle = i * 5f
                    val angleRad = (angle - 90) * (PI / 180f).toFloat()
                    val dotRadius = radius * 0.9f
                    drawCircle(
                        color = if (i % 18 == 0) colors.textPrimary else colors.textMuted,
                        radius = 1.5.dp.toPx(),
                        center = Offset(
                            x = center.x + dotRadius * cos(angleRad),
                            y = center.y + dotRadius * sin(angleRad)
                        )
                    )
                }

                val cardinalRadius = radius * 1.18f
                val cardinals = listOf("U" to 0f, "T" to 90f, "S" to 180f, "B" to 270f)

                cardinals.forEach { (label, angle) ->
                    val textLayoutResult = textMeasurer.measure(label, textStyle)
                    val angleRad = (angle - 90) * (PI / 180f).toFloat()
                    val x = center.x + cardinalRadius * cos(angleRad) - textLayoutResult.size.width / 2
                    val y = center.y + cardinalRadius * sin(angleRad) - textLayoutResult.size.height / 2

                    rotate(degrees = heading, pivot = Offset(x + textLayoutResult.size.width / 2, y + textLayoutResult.size.height / 2)) {
                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(x, y)
                        )
                    }
                }

                if (bearingToKaaba != null && !isLoading) {
                    rotate(degrees = bearingToKaaba, pivot = center) {
                        // Tail
                        drawLine(
                            color = Color(0xFF3B4844),
                            start = center,
                            end = Offset(center.x, center.y + radius * 0.3f),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Pointer
                        drawLine(
                            color = colors.accentGold,
                            start = center,
                            end = Offset(center.x, center.y - radius * 0.65f),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Center Pivot
                        drawCircle(
                            color = colors.accentGold,
                            radius = 6.dp.toPx(),
                            center = center
                        )

                        // Head
                        val headCenter = Offset(center.x, center.y - radius * 0.65f)
                        val headRadius = 16.dp.toPx()
                        
                        drawCircle(
                            color = colors.accentGold,
                            radius = headRadius,
                            center = headCenter
                        )

                        translate(top = headCenter.y, left = headCenter.x) {
                            val kaabaSize = 14.dp.toPx()
                            val halfSize = kaabaSize / 2

                            drawRect(
                                color = Color.Black,
                                topLeft = Offset(-halfSize, -halfSize),
                                size = Size(kaabaSize, kaabaSize)
                            )
                            drawRect(
                                color = colors.accentGold,
                                topLeft = Offset(-halfSize, -halfSize + 2.dp.toPx()),
                                size = Size(kaabaSize, 2.dp.toPx())
                            )
                            drawRect(
                                color = colors.accentGold,
                                topLeft = Offset(-halfSize + kaabaSize * 0.6f, -halfSize + 6.dp.toPx()),
                                size = Size(3.dp.toPx(), kaabaSize - 6.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
    }
}
