package com.c0d3in3.btuclassroom.ui.courses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoursesViewModel @Inject constructor(userRepository: UserRepository): BaseViewModel() {

    val courses : MutableLiveData<List<Course>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            courses.postValue(userRepository.getUser().courses)
        }
    }
}