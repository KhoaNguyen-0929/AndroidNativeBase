package vn.start.pomodoro.completion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import vn.start.domain.model.PomodoroSessionType

/**
 * Session Completion Dialog - The dopamine loop!
 * 
 * UX Goal: Create positive reinforcement without gamification bloat.
 */
@Composable
fun SessionCompletionDialog(
    sessionType: PomodoroSessionType,
    completedSessionsToday: Int,
    totalFocusMinutesToday: Int,
    onStartNextSession: () -> Unit,
    onDismiss: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .scale(scale.value),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
            ) {
                CompletionContent(
                    sessionType = sessionType,
                    completedSessionsToday = completedSessionsToday,
                    totalFocusMinutesToday = totalFocusMinutesToday,
                    onStartNextSession = onStartNextSession,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun CompletionContent(
    sessionType: PomodoroSessionType,
    completedSessionsToday: Int,
    totalFocusMinutesToday: Int,
    onStartNextSession: () -> Unit,
    onDismiss: () -> Unit
) {
    val (title, emoji, message, nextAction) = when (sessionType) {
        PomodoroSessionType.FOCUS -> CompletionData(
            title = "Nice work! 👏",
            emoji = "🎉",
            message = "You crushed that focus session!",
            nextAction = "Start Break"
        )
        PomodoroSessionType.SHORT_BREAK -> CompletionData(
            title = "Break over!",
            emoji = "💪",
            message = "Ready to get back to work?",
            nextAction = "Start Focus"
        )
        PomodoroSessionType.LONG_BREAK -> CompletionData(
            title = "Well rested!",
            emoji = "🌟",
            message = "Time for another productive cycle!",
            nextAction = "Start Focus"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Big celebration emoji
        Text(
            text = emoji,
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Message
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats for focus sessions
        if (sessionType == PomodoroSessionType.FOCUS) {
            StatsRow(
                completedSessions = completedSessionsToday,
                totalMinutes = totalFocusMinutesToday
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Primary action button
        Button(
            onClick = onStartNextSession,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = nextAction,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Skip button
        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Not now",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun StatsRow(
    completedSessions: Int,
    totalMinutes: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = "$completedSessions",
                label = "Sessions today",
                emoji = "🍅"
            )
            StatItem(
                value = "${totalMinutes}m",
                label = "Focused today",
                emoji = "⏱️"
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$emoji $value",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

private data class CompletionData(
    val title: String,
    val emoji: String,
    val message: String,
    val nextAction: String
)
