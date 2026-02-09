package vn.start.pomodoro.summary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Daily Summary Card - Shows today's focus stats
 * 
 * MVP Rules:
 * - Today view only (no graphs)
 * - No weekly/monthly views
 * - Simple text insight
 */
@Composable
fun DailySummaryCard(
    completedSessions: Int,
    totalFocusMinutes: Int,
    yesterdayFocusMinutes: Int = 0,
    modifier: Modifier = Modifier
) {
    var showCard by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        showCard = true
    }

    AnimatedVisibility(
        visible = showCard,
        enter = fadeIn() + slideInVertically { it }
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Text(
                    text = "Today's Focus",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatBox(
                        value = "$completedSessions",
                        label = "Pomodoros",
                        emoji = "🍅"
                    )
                    StatBox(
                        value = formatDuration(totalFocusMinutes),
                        label = "Focused",
                        emoji = "⏱️"
                    )
                }

                // Insight text
                if (completedSessions > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InsightText(
                        todayMinutes = totalFocusMinutes,
                        yesterdayMinutes = yesterdayFocusMinutes
                    )
                }
            }
        }
    }
}

@Composable
private fun StatBox(
    value: String,
    label: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
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

@Composable
private fun InsightText(
    todayMinutes: Int,
    yesterdayMinutes: Int,
    modifier: Modifier = Modifier
) {
    val insight = when {
        yesterdayMinutes == 0 && todayMinutes > 0 -> "🎯 Great start today!"
        todayMinutes > yesterdayMinutes -> "📈 You focused ${todayMinutes - yesterdayMinutes}m more than yesterday!"
        todayMinutes == yesterdayMinutes -> "✨ Same focus as yesterday - keep it up!"
        todayMinutes < yesterdayMinutes -> "💪 Keep going! ${yesterdayMinutes - todayMinutes}m to match yesterday."
        else -> "🌟 Every session counts!"
    }

    Text(
        text = insight,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
        modifier = modifier
    )
}

private fun formatDuration(minutes: Int): String {
    return when {
        minutes < 60 -> "${minutes}m"
        minutes % 60 == 0 -> "${minutes / 60}h"
        else -> "${minutes / 60}h ${minutes % 60}m"
    }
}

/**
 * Compact version of daily summary for showing in the main screen
 */
@Composable
fun DailySummaryCompact(
    completedSessions: Int,
    totalFocusMinutes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "🍅 $completedSessions sessions • ${formatDuration(totalFocusMinutes)} focused today",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
