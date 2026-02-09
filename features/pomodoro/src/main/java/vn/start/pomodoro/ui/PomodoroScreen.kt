package vn.start.pomodoro.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vn.start.domain.model.PomodoroSessionType

/**
 * Home Screen - The Heart of the Pomodoro App
 * 
 * UX Principle: One primary action only.
 * - Large central Start/Pause/Resume button
 * - Big timer display
 * - Minimal distractions
 */
@Composable
fun PomodoroScreen(
    viewModel: PomodoroViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Collect effects
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PomodoroContract.UiEffect.ShowSnackbar -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is PomodoroContract.UiEffect.SessionCompleted -> {
                    // Haptic feedback on completion
                    triggerHapticFeedback(context, isStrong = true)
                    Toast.makeText(context, "Focus session complete! 🎉", Toast.LENGTH_LONG).show()
                }
                is PomodoroContract.UiEffect.BreakCompleted -> {
                    triggerHapticFeedback(context, isStrong = true)
                    Toast.makeText(context, "Break over! Time to focus!", Toast.LENGTH_LONG).show()
                }
                is PomodoroContract.UiEffect.ShowCompletionDialog -> {
                    // Handle completion dialog - for now just show toast
                    // Could show a dialog with "Start next" button
                }
                is PomodoroContract.UiEffect.NavigateToSettings -> onNavigateToSettings()
                is PomodoroContract.UiEffect.NavigateToHistory -> onNavigateToHistory()
                is PomodoroContract.UiEffect.PlaySound -> {
                    // TODO: Implement sound playing
                }
            }
        }
    }

    PomodoroHomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun PomodoroHomeContent(
    uiState: PomodoroContract.UiState,
    onEvent: (PomodoroContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Background color based on session type
    val backgroundColor by animateColorAsState(
        targetValue = when (uiState.sessionType) {
            PomodoroSessionType.FOCUS -> MaterialTheme.colorScheme.background
            PomodoroSessionType.SHORT_BREAK -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            PomodoroSessionType.LONG_BREAK -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        },
        animationSpec = tween(500),
        label = "bgColor"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Session type chip (minimal)
            SessionTypeChip(sessionType = uiState.sessionType)
            
            // Task name display (if set and running/paused)
            if (!uiState.taskName.isNullOrBlank() && (uiState.isRunning || uiState.isPaused)) {
                Spacer(modifier = Modifier.height(8.dp))
                TaskDisplay(taskName = uiState.taskName)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BIG Timer Display
            TimerDisplay(
                formattedTime = uiState.formattedTime,
                progress = uiState.progress,
                sessionType = uiState.sessionType
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Task input field (only when idle and Focus session)
            if (uiState.isIdle && uiState.sessionType == PomodoroSessionType.FOCUS) {
                TaskInputField(
                    taskName = uiState.taskName ?: "",
                    onTaskNameChange = { name ->
                        onEvent(PomodoroContract.UiEvent.LinkToTask(null, name.ifBlank { null }))
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // GIANT Primary Button
            GiantActionButton(
                uiState = uiState,
                onStart = { 
                    triggerHapticFeedback(context, isStrong = false)
                    onEvent(PomodoroContract.UiEvent.Start) 
                },
                onPause = { 
                    triggerHapticFeedback(context, isStrong = false)
                    onEvent(PomodoroContract.UiEvent.Pause) 
                },
                onResume = { 
                    triggerHapticFeedback(context, isStrong = false)
                    onEvent(PomodoroContract.UiEvent.Resume) 
                }
            )

            // Stop button (only when running/paused)
            if (uiState.isRunning || uiState.isPaused) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Skip button (small, non-primary)
                    TextButton(onClick = { onEvent(PomodoroContract.UiEvent.Skip) }) {
                        Text(
                            text = "Skip",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StopButton(onClick = { onEvent(PomodoroContract.UiEvent.Stop) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cycle indicator (●●●○)
            CycleIndicator(
                currentCycle = uiState.currentCycle,
                totalCycles = uiState.totalCycles
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Status message
            StatusMessage(uiState = uiState)
        }
    }
}

@Composable
private fun SessionTypeChip(
    sessionType: PomodoroSessionType,
    modifier: Modifier = Modifier
) {
    val (label, emoji) = when (sessionType) {
        PomodoroSessionType.FOCUS -> "Focus" to "🍅"
        PomodoroSessionType.SHORT_BREAK -> "Short Break" to "☕"
        PomodoroSessionType.LONG_BREAK -> "Long Break" to "🌴"
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Text(
            text = "$emoji $label",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun TimerDisplay(
    formattedTime: String,
    progress: Float,
    sessionType: PomodoroSessionType,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = "progress"
    )

    val primaryColor = when (sessionType) {
        PomodoroSessionType.FOCUS -> MaterialTheme.colorScheme.primary
        PomodoroSessionType.SHORT_BREAK -> MaterialTheme.colorScheme.secondary
        PomodoroSessionType.LONG_BREAK -> MaterialTheme.colorScheme.tertiary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(300.dp)
    ) {
        // Background circle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.1f),
                            primaryColor.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        // Progress ring
        androidx.compose.material3.CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = primaryColor,
            strokeWidth = 8.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Time text - BIG and BOLD
        Text(
            text = formattedTime,
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun GiantActionButton(
    uiState: PomodoroContract.UiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonSize by animateDpAsState(
        targetValue = if (uiState.isRunning) 100.dp else 120.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonSize"
    )

    val buttonColor by animateColorAsState(
        targetValue = when {
            uiState.isRunning -> MaterialTheme.colorScheme.secondary
            uiState.isPaused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(300),
        label = "buttonColor"
    )

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(buttonSize)
            .scale(scale)
            .clip(CircleShape)
            .background(buttonColor)
    ) {
        IconButton(
            onClick = {
                when {
                    uiState.isIdle || uiState.isCompleted -> onStart()
                    uiState.isRunning -> onPause()
                    uiState.isPaused -> onResume()
                }
            },
            modifier = Modifier.fillMaxSize(),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            AnimatedContent(
                targetState = when {
                    uiState.isRunning -> "pause"
                    else -> "play"
                },
                transitionSpec = {
                    (scaleIn(animationSpec = spring()) + fadeIn())
                        .togetherWith(scaleOut() + fadeOut())
                },
                label = "icon"
            ) { state ->
                Icon(
                    imageVector = if (state == "pause") Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state == "pause") "Pause" else "Start",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun StopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            imageVector = Icons.Default.Stop,
            contentDescription = "Stop",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun StatusMessage(
    uiState: PomodoroContract.UiState,
    modifier: Modifier = Modifier
) {
    val message = when {
        uiState.isIdle -> "Tap to start focusing"
        uiState.isRunning -> when (uiState.sessionType) {
            PomodoroSessionType.FOCUS -> "Stay focused... You got this! 💪"
            PomodoroSessionType.SHORT_BREAK -> "Stand up. Breathe. 🌿"
            PomodoroSessionType.LONG_BREAK -> "Take a well-deserved break! 🌴"
        }
        uiState.isPaused -> "Paused"
        uiState.isCompleted -> "Session complete! 🎉"
        else -> ""
    }

    AnimatedContent(
        targetState = message,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "status"
    ) { text ->
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Task input field - "What are you working on?"
 */
@Composable
private fun TaskInputField(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedTextField(
        value = taskName,
        onValueChange = onTaskNameChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = "What are you working on?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

/**
 * Task display during active session
 */
@Composable
private fun TaskDisplay(
    taskName: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "📋 $taskName",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

/**
 * Trigger haptic feedback
 */
private fun triggerHapticFeedback(context: Context, isStrong: Boolean) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    vibrator?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = if (isStrong) {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            } else {
                VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            it.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(if (isStrong) 100L else 30L)
        }
    }
}

/**
 * Cycle indicator showing progress toward long break (●●●○)
 */
@Composable
private fun CycleIndicator(
    currentCycle: Int,
    totalCycles: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalCycles) { index ->
            val isFilled = index < currentCycle
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isFilled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}

