package com.c0d3in3.btuclassroom.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.App
import ge.ted3x.core.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture
import java.util.*

class DashboardViewModel : ge.ted3x.core.base.BaseViewModel() {

    val user = App.currentUser
    val nextLecture = MutableLiveData<Lecture>()
    val yearText = MutableLiveData<String>()

    init {
        nextLecture.value = getNextLecture()
        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        yearText.value = "$curYear - ${curYear + 1}"
    }

    private fun getNextLecture() : Lecture{
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val lecture : Lecture? = null
        user.lectures.forEach {
            if (it.day != null && it.startTime != null) {
                val parsed = it.startTime.split(":")
                if((day < it.day) || (day == it.day && hour < parsed[0].toInt() && minute < parsed[1].toInt())){
                    nextLecture.postValue(it)
                    return@forEach
                }
            }
        }
        return lecture ?: user.lectures[0]
    }
}
