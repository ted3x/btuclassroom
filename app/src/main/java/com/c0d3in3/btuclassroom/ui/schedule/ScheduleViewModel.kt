package com.c0d3in3.btuclassroom.ui.schedule

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture

class ScheduleViewModel : BaseViewModel() {
    var schedule : MutableLiveData<List<Lecture>> = MutableLiveData()

    init {
        schedule.value = App.currentUser.lectures
    }

}