package vn.start.pomodoro.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingsScreen(
    viewModel: PomodoroSettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PomodoroSettingsContract.UiEffect.NavigateBack -> onNavigateBack()
                is PomodoroSettingsContract.UiEffect.ShowSnackbar -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(PomodoroSettingsContract.UiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        PomodoroSettingsContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun PomodoroSettingsContent(
    uiState: PomodoroSettingsContract.UiState,
    onEvent: (PomodoroSettingsContract.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Timer Durations Section
        SectionHeader("Timer Durations")

        DurationSlider(
            label = "Focus Duration",
            value = uiState.focusDurationMinutes,
            valueRange = 1f..60f,
            onValueChange = { onEvent(PomodoroSettingsContract.UiEvent.SetFocusDuration(it)) }
        )

        DurationSlider(
            label = "Short Break",
            value = uiState.shortBreakDurationMinutes,
            valueRange = 1f..30f,
            onValueChange = { onEvent(PomodoroSettingsContract.UiEvent.SetShortBreakDuration(it)) }
        )

        DurationSlider(
            label = "Long Break",
            value = uiState.longBreakDurationMinutes,
            valueRange = 5f..60f,
            onValueChange = { onEvent(PomodoroSettingsContract.UiEvent.SetLongBreakDuration(it)) }
        )

        SessionsSlider(
            label = "Sessions until Long Break",
            value = uiState.sessionsUntilLongBreak,
            valueRange = 2f..8f,
            onValueChange = { onEvent(PomodoroSettingsContract.UiEvent.SetSessionsUntilLongBreak(it)) }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Auto-Start Section
        SectionHeader("Auto-Start")

        SwitchSetting(
            label = "Auto-start Breaks",
            description = "Automatically start break when focus ends",
            checked = uiState.autoStartBreaks,
            onCheckedChange = { onEvent(PomodoroSettingsContract.UiEvent.SetAutoStartBreaks(it)) }
        )

        SwitchSetting(
            label = "Auto-start Focus",
            description = "Automatically start focus when break ends",
            checked = uiState.autoStartFocus,
            onCheckedChange = { onEvent(PomodoroSettingsContract.UiEvent.SetAutoStartFocus(it)) }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Notifications Section
        SectionHeader("Notifications")

        SwitchSetting(
            label = "Sound",
            description = "Play sound when session ends",
            checked = uiState.soundEnabled,
            onCheckedChange = { onEvent(PomodoroSettingsContract.UiEvent.SetSoundEnabled(it)) }
        )

        SwitchSetting(
            label = "Vibration",
            description = "Vibrate when session ends",
            checked = uiState.vibrationEnabled,
            onCheckedChange = { onEvent(PomodoroSettingsContract.UiEvent.SetVibrationEnabled(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reset Button
        OutlinedButton(
            onClick = { onEvent(PomodoroSettingsContract.UiEvent.ResetToDefaults) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset to Defaults")
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun DurationSlider(
    label: String,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "$value min",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) - 1).toInt()
        )
    }
}

@Composable
private fun SessionsSlider(
    label: String,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "$value",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) - 1).toInt()
        )
    }
}

@Composable
private fun SwitchSetting(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
