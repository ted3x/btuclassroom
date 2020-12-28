package com.c0d3in3.btuclassroom.ui.dashboard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DashboardViewModel @Inject constructor(userRepository: UserRepository) : BaseViewModel() {

    val user = MutableLiveData<User>()
    val nextLecture = MutableLiveData<Lecture>()
    val yearText = MutableLiveData<String>()
    val userImage = MutableLiveData<Bitmap>()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val userData = userRepository.getUser()
            withContext(Dispatchers.Main) {
                user.value = userData
            }
            if (user.value?.userImage != null) {
                //TODO EXTENSION decodeAndSetImage
                val bitmap = BitmapFactory.decodeByteArray(
                    user.value!!.userImage,
                    0,
                    (user.value!!.userImage!!.size)
                )
                userImage.postValue(bitmap)
            }
            nextLecture.postValue(getNextLecture())
        }
        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        yearText.value = "$curYear - ${curYear + 1}"
    }

    private fun getNextLecture(): Lecture {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val lecture: Lecture? = null
        user.value!!.lectures.forEach {
            if (it.day != null && it.startTime != null) {
                val parsed = it.startTime.split(":")
                if ((day < it.day) || (day == it.day && hour < parsed[0].toInt() && minute < parsed[1].toInt())) {
                    nextLecture.postValue(it)
                    return@forEach
                }
            }
        }
        return lecture ?: user.value!!.lectures[0]
    }
}
