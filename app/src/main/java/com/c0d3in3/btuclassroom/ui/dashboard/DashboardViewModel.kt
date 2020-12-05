package com.c0d3in3.btuclassroom.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.schedule.Lecture
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler.SCHEDULE_URL
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import com.c0d3in3.btuclassroom.utils.getDayInt
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel : BaseViewModel() {

    companion object {
        const val FIRST_COURSE = "I კურსი"
        const val SECOND_COURSE = "II კურსი"
        const val THIRD_COURSE = "III კურსი"
        const val FOURTH_COURSE = "IV კურსი"
        const val FIRST_TIME = "first_time"
    }

    val userCredits = MutableLiveData<String>()
    val user = MutableLiveData<User>()
    val nextLecture = MutableLiveData<Lecture>()
    val yearText = MutableLiveData<String>()

    init {

        viewModelScope.launch {
            withContext(Dispatchers.IO) { user.postValue(App.userRepository.getUser())  }
            getCredits()
        }
        //if (SharedPreferencesHandler.getBooleanSP(FIRST_TIME)) createSchedule()
        //else getNextLecture()

        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        yearText.value = "$curYear - ${curYear + 1}"
    }

    private fun getCredits() {
        when (user.value!!.userCredits) {
            in 60..119 -> userCredits.value = SECOND_COURSE
            in 120..179 -> userCredits.value = THIRD_COURSE
            in 180..240 -> userCredits.value = FOURTH_COURSE
            else -> userCredits.value = FIRST_COURSE
        }
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

    private fun getNextLecture(){
        val lectures = App.lectureRepository.getLectures()
    }
}
