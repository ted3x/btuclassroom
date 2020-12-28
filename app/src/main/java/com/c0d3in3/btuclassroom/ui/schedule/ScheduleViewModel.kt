package com.c0d3in3.btuclassroom.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.model.Lecture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(userRepository: UserRepository) : BaseViewModel() {
    var schedule : MutableLiveData<List<Lecture>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            schedule.postValue(userRepository.getUser().lectures)
        }
    }
}