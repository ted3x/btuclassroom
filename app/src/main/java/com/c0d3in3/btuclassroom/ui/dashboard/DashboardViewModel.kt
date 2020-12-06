package com.c0d3in3.btuclassroom.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.data.local.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel : BaseViewModel() {



    val user = MutableLiveData<User>()
    val nextLecture = MutableLiveData<Lecture>()
    val yearText = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val scope = viewModelScope.async(Dispatchers.IO) {
                user.postValue(App.userRepository.getUser())
            }
            scope.await()
            getNextLecture()
        }
        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        yearText.value = "$curYear - ${curYear + 1}"
    }

    /*private fun getMail() {
        val mailNumber =
            parsedData.select("a[href=\"https://classroom.btu.edu.ge/ge/messages\"]").select("span")
                .text()
        if (getLongCache(this, "mailNumber") < mailNumber.toLong()) {
            val unreadDrawable = resources.getDrawable(R.drawable.ic_mails_unread)
            mailButton.setCompoundDrawablesWithIntrinsicBounds(unreadDrawable, null, null, null)
        }
    }*/

    private fun getNextLecture() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val lectures = user.value?.lectures
        lectures?.forEach {
            if (it.day != null && it.startTime != null) {
                val parsed = it.startTime.split(":")
                if((day < it.day) || (day == it.day && hour < parsed[0].toInt() && minute < parsed[1].toInt())){
                    nextLecture.postValue(it)
                    return
                }
            }
        }
    }
}
