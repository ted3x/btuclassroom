package com.c0d3in3.btuclassroom.ui.courses

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.App
import ge.ted3x.core.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Course

class CoursesViewModel : ge.ted3x.core.base.BaseViewModel() {

    val courses : MutableLiveData<List<Course>> = MutableLiveData()

    init {
        courses.value = App.currentUser.courses
    }
}