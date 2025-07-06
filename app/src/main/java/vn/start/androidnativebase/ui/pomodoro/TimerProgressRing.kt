package vn.start.androidnativebase.ui.pomodoro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI

@Composable
fun TimerProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 8f,
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(
        modifier = modifier.size(300.dp)
    ) {
        val radius = (size.minDimension - strokeWidth) / 2
        val center = Offset(size.width / 2, size.height / 2)

        // Background circle
        drawCircle(
            color = backgroundColor,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress arc
        val sweepAngle = progress * 2 * PI.toFloat()
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = sweepAngle * 180f / PI.toFloat(),
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
} 