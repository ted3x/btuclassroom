package com.c0d3in3.btuclassroom.ui.schedule

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.App
import ge.ted3x.core.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture

class ScheduleViewModel : ge.ted3x.core.base.BaseViewModel() {
    var schedule : MutableLiveData<List<Lecture>> = MutableLiveData()

    init {
        schedule.value = App.currentUser.lectures
    }

}