package com.c0d3in3.btuclassroom.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){
    val message = MutableLiveData<String>()
}