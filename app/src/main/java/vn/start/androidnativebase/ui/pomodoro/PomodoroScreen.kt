package vn.start.androidnativebase.ui.pomodoro

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.start.androidnativebase.R
import vn.start.androidnativebase.ui.theme.AndroidNativeBaseTheme
import vn.start.androidnativebase.ui.theme.CoralOrange
import vn.start.androidnativebase.ui.theme.MintGreen
import vn.start.androidnativebase.ui.theme.SoftBlue
import vn.start.androidnativebase.ui.theme.SoftOrange
import vn.start.androidnativebase.ui.theme.TomatoRed

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    viewModel: PomodoroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        TopAppBar()

        Spacer(modifier = Modifier.height(32.dp))

        // Timer Display
        TimerDisplay(
            timeLeft = uiState.timeLeft,
            progress = uiState.progress,
            isRunning = uiState.isRunning
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Session Type Display
        SessionTypeDisplay(sessionType = uiState.currentSession)

        Spacer(modifier = Modifier.height(48.dp))

        // Timer Controls
        TimerControls(
            isRunning = uiState.isRunning,
            onStartPause = {
                if (uiState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
            },
            onReset = { viewModel.resetTimer() },
            onSkip = { viewModel.skipTimer() }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Pomodoro Count
        PomodoroCount(count = uiState.pomodoroCount)
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pomodoro Timer",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        // Settings icon
        IconButton(onClick = { /* TODO: Settings */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun TimerDisplay(
    timeLeft: String,
    progress: Float,
    isRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Progress Ring
        TimerProgressRing(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 12f,
            backgroundColor = Color.White.copy(alpha = 0.2f),
            progressColor = if (isRunning) Color.White else Color.White.copy(alpha = 0.7f)
        )

        // Timer Circle with gradient
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isRunning) {
                            listOf(CoralOrange, TomatoRed)
                        } else {
                            listOf(SoftOrange, CoralOrange)
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeLeft,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SessionTypeDisplay(
    sessionType: SessionType,
    modifier: Modifier = Modifier
) {
    val sessionText = when (sessionType) {
        SessionType.Focus -> "Focus Session"
        SessionType.Break -> "Short Break"
        SessionType.LongBreak -> "Long Break"
    }

    val sessionColor = when (sessionType) {
        SessionType.Focus -> TomatoRed
        SessionType.Break -> MintGreen
        SessionType.LongBreak -> SoftBlue
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = sessionColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = sessionText,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = sessionColor
            )
        )
    }
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    onStartPause: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start/Pause Button
        FloatingActionButton(
            onClick = onStartPause,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                painter = if (isRunning) painterResource(R.drawable.ic_pause) else painterResource(R.drawable.ic_play_arrow),
                contentDescription = if (isRunning) "Pause" else "Start",
                modifier = Modifier.size(24.dp)
            )
        }

        // Reset Button
        IconButton(
            onClick = onReset,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Skip Button
        IconButton(
            onClick = onSkip,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_next),
                contentDescription = "Skip",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PomodoroCount(
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🍅",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "x $count",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroScreenPreview() {
    AndroidNativeBaseTheme {
        PomodoroScreen()
    }
} 