package vn.start.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.start.common.UiState
import vn.start.common.toUiState
import vn.start.domain.model.DemoModel
import vn.start.domain.usecase.GetDemoUseCase
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val getDemoUseCase: GetDemoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DemoModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<DemoModel>> = _uiState.asStateFlow()

    init {
        loadDemo()
    }

    fun loadDemo() {
        viewModelScope.launch {
            getDemoUseCase().collectLatest { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun retry() {
        _uiState.value = UiState.Loading
        loadDemo()
    }
}
