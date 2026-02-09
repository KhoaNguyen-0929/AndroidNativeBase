package vn.start.pomodoro.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { uiState.totalPages })

    // Sync pager with state
    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    // Sync state with pager
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onEvent(OnboardingContract.UiEvent.GoToPage(page))
        }
    }

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is OnboardingContract.UiEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { viewModel.onEvent(OnboardingContract.UiEvent.Skip) }) {
                    Text("Skip")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(
                    page = OnboardingContract.pages[page],
                    isLastPage = page == uiState.totalPages - 1,
                    selectedDuration = uiState.selectedDurationMinutes,
                    onDurationSelected = { 
                        viewModel.onEvent(OnboardingContract.UiEvent.SelectDuration(it)) 
                    }
                )
            }

            // Page indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                repeat(uiState.totalPages) { index ->
                    val color by animateColorAsState(
                        targetValue = if (index == uiState.currentPage) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant,
                        label = "indicator"
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (index == uiState.currentPage) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action button
            AnimatedContent(
                targetState = uiState.isLastPage,
                label = "button"
            ) { isLastPage ->
                if (isLastPage) {
                    Button(
                        onClick = { viewModel.onEvent(OnboardingContract.UiEvent.GetStarted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Get Started 🚀",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.onEvent(OnboardingContract.UiEvent.NextPage) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Next",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OnboardingPage(
    page: OnboardingContract.OnboardingPage,
    isLastPage: Boolean,
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Emoji
        Text(
            text = page.emoji,
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Duration picker on last page
        if (isLastPage) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Choose your focus duration:",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OnboardingContract.durationOptions.forEach { duration ->
                    FilterChip(
                        selected = duration == selectedDuration,
                        onClick = { onDurationSelected(duration) },
                        label = { Text("${duration}m") }
                    )
                }
            }
        }
    }
}
