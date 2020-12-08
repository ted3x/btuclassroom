package com.c0d3in3.btuclassroom.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleViewModel : BaseViewModel() {
    var schedule : MutableLiveData<List<Lecture>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            schedule.postValue(App.userRepository.getUser().lectures)
        }
    }

}