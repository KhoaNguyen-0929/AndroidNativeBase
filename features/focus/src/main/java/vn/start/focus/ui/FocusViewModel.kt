package vn.start.focus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(): ViewModel() {

    fun insertSchedule(){
        viewModelScope.launch {
//            useCase.invoke(ScheduleModel(1,1,1000,6000))
        }
    }

}