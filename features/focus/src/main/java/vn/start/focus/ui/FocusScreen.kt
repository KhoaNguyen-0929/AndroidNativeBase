package vn.start.focus.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FocusScreen(viewModel: FocusViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.insertSchedule()
    }
}
