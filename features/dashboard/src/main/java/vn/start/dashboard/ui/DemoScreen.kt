package vn.start.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import vn.start.common.UiState
import vn.start.domain.model.DemoModel

/**
 * Complete demo screen showing Single Source of Truth strategies.
 *
 * This screen demonstrates:
 * 1. Cache-first strategy - shows data instantly, refreshes in background
 * 2. Network-only strategy - always fetches fresh data
 * 3. Manual refresh functionality
 * 4. All UI states (Loading, Success, Error, Idle)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen(
    navController: NavHostController? = null,
    viewModel: DemoViewModel = hiltViewModel()
) {
    val cachedUiState by viewModel.cachedUiState.collectAsState()
    val freshUiState by viewModel.freshUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Single Source of Truth Demo") },
                navigationIcon = {
                    if (navController != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Demo Information",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This demo shows two data fetching strategies:\n\n" +
                                  "• Cache-First: Instant UI from cache, background refresh\n" +
                                  "• Network-Only: Always fresh data from network\n\n" +
                                  "Current data source: Mock API (1s delay)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Action Buttons
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Actions",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.loadCachedDemo() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Load Cached")
                            }
                            Button(
                                onClick = { viewModel.loadFreshDemo() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Load Fresh")
                            }
                        }

                        Button(
                            onClick = { viewModel.refreshDemo() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Force Refresh (Clears Cache)")
                        }
                    }
                }
            }

            // Cache-First Strategy Section
            item {
                StrategyCard(
                    title = "Cache-First Strategy",
                    description = "Shows cached data instantly, refreshes in background",
                    features = listOf(
                        "✓ Instant UI response",
                        "✓ Background refresh",
                        "✓ Works offline"
                    ),
                    uiState = cachedUiState
                )
            }

            // Network-Only Strategy Section
            item {
                StrategyCard(
                    title = "Network-Only Strategy",
                    description = "Always fetches fresh data from network",
                    features = listOf(
                        "⏱ Always shows loading",
                        "✓ Fresh data",
                        "✗ No caching"
                    ),
                    uiState = freshUiState
                )
            }

            // Flow Diagram
            item {
                FlowDiagramCard()
            }
        }
    }
}

@Composable
private fun StrategyCard(
    title: String,
    description: String,
    features: List<String>,
    uiState: UiState<DemoModel>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title and Description
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Features
            features.forEach { feature ->
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            // State Display
            StateContent(uiState = uiState)
        }
    }
}

@Composable
private fun StateContent(uiState: UiState<DemoModel>) {
    when (uiState) {
        is UiState.Idle -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💤 Idle",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Click a button to load data",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Loading -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        is UiState.Success -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✓ Success",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                    DemoDataRow(label = "ID", value = uiState.data.id)
                    DemoDataRow(label = "Name", value = uiState.data.name)
                    DemoDataRow(label = "Duration", value = "${uiState.data.durationMinutes} minutes")
                }
            }
        }
        is UiState.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "✗ Error",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun DemoDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun FlowDiagramCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Cache-First Flow Diagram",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f)
            )
            FlowStep(number = "1", text = "Emit Loading state")
            FlowStep(number = "2", text = "Fetch from cache → emit if available")
            FlowStep(number = "3", text = "Fetch from network")
            FlowStep(number = "4", text = "Save response to cache")
            FlowStep(number = "5", text = "Emit final result")
        }
    }
}

@Composable
private fun FlowStep(number: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.primary,
            shape = androidx.compose.foundation.shape.CircleShape
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}
