package ge.ted3x.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){
    val message = MutableLiveData<String>()
}