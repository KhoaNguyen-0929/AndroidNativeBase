package vn.start.dashboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DashboardScreen(viewModel: DashBoardViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.insertSchedule()
    }
}
