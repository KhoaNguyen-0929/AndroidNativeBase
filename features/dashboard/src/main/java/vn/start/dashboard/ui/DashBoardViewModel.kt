package vn.start.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.start.domain.model.ScheduleModel
import vn.start.domain.usecase.schedule.AddScheduleUseCase
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(private val useCase: AddScheduleUseCase): ViewModel() {

    fun insertSchedule(){
        viewModelScope.launch {
//            useCase.invoke(ScheduleModel(1,1,1000,6000))
        }
    }

}