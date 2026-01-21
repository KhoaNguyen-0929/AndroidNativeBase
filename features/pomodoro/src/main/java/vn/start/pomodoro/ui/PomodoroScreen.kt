package vn.start.pomodoro.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.start.domain.model.PomodoroSessionType

/**
 * Entry composable for the Pomodoro screen.
 * Collects state from ViewModel and delegates to stateless PomodoroContent.
 */
@Composable
fun PomodoroScreen(
    viewModel: PomodoroViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PomodoroContent(
        uiState = uiState,
        onStartClick = viewModel::start,
        onPauseClick = viewModel::pause,
        onResumeClick = viewModel::resume,
        onStopClick = viewModel::stop
    )
}

/**
 * Stateless content composable for the Pomodoro screen.
 * UI is a pure function of state - no business or timer logic.
 */
@Composable
fun PomodoroContent(
    uiState: PomodoroUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Session type label
            SessionTypeLabel(sessionType = uiState.sessionType)

            Spacer(modifier = Modifier.height(48.dp))

            // Timer display with progress
            TimerDisplay(
                formattedTime = uiState.formattedTime,
                progress = uiState.progress
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Action buttons
            ActionButtons(
                uiState = uiState,
                onStartClick = onStartClick,
                onPauseClick = onPauseClick,
                onResumeClick = onResumeClick,
                onStopClick = onStopClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Status message
            StatusMessage(uiState = uiState)
        }
    }
}

/**
 * Displays the current session type (Focus, Short Break, Long Break).
 */
@Composable
private fun SessionTypeLabel(
    sessionType: PomodoroSessionType,
    modifier: Modifier = Modifier
) {
    val label = when (sessionType) {
        PomodoroSessionType.FOCUS -> "Focus Session"
        PomodoroSessionType.SHORT_BREAK -> "Short Break"
        PomodoroSessionType.LONG_BREAK -> "Long Break"
    }

    Text(
        text = label,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

/**
 * Displays the timer with circular progress indicator.
 */
@Composable
private fun TimerDisplay(
    formattedTime: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(280.dp)
    ) {
        // Background circle
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 12.dp,
            strokeCap = StrokeCap.Round
        )

        // Progress circle
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 12.dp,
            strokeCap = StrokeCap.Round
        )

        // Time text
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * Action buttons based on session status.
 */
@Composable
private fun ActionButtons(
    uiState: PomodoroUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        when {
            uiState.showStartButton -> {
                // Start button
                FilledIconButton(
                    onClick = onStartClick,
                    modifier = Modifier.size(72.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            uiState.showPauseButton -> {
                // Pause button
                FilledIconButton(
                    onClick = onPauseClick,
                    modifier = Modifier.size(72.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Stop button
                OutlinedIconButton(
                    onClick = onStopClick,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            uiState.showResumeButton -> {
                // Resume button
                FilledIconButton(
                    onClick = onResumeClick,
                    modifier = Modifier.size(72.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Stop button
                OutlinedIconButton(
                    onClick = onStopClick,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            uiState.isCompleted -> {
                // Restart button after completion
                FilledIconButton(
                    onClick = onStartClick,
                    modifier = Modifier.size(72.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start New Session",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

/**
 * Status message based on session state.
 */
@Composable
private fun StatusMessage(
    uiState: PomodoroUiState,
    modifier: Modifier = Modifier
) {
    val message = when {
        uiState.isIdle -> "Ready to focus"
        uiState.isRunning -> "Stay focused..."
        uiState.isPaused -> "Paused"
        uiState.isCompleted -> "Session complete! 🎉"
        else -> ""
    }

    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
