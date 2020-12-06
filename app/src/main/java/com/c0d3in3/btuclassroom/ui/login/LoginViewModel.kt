package com.c0d3in3.btuclassroom.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.Constants
import com.c0d3in3.btuclassroom.Constants.COOKIES
import com.c0d3in3.btuclassroom.Constants.PASSWORD
import com.c0d3in3.btuclassroom.Constants.USERNAME
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler.SCHEDULE_URL
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider.getResourceString
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import com.c0d3in3.btuclassroom.utils.getDayInt
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    companion object {
        const val FIRST_TIME = "first_time"
    }

    private val username: String
    private val password: String
    val captcha = MutableLiveData<ByteArray>()
    val auth = MutableLiveData<Boolean>()
    val loadingMessage = MutableLiveData<String>()
    val firstTimeUser : Boolean

    init {
        SharedPreferencesHandler.removeSP(COOKIES)
        username = SharedPreferencesHandler.getStringSP(USERNAME)
        password = SharedPreferencesHandler.getStringSP(PASSWORD)
        firstTimeUser = SharedPreferencesHandler.getBooleanSP(FIRST_TIME)
        if (username.isNotBlank() && password.isNotBlank()) {
            if(isNetworkAvailable()) logIn(username, password)
            else auth.value = true
        }
    }

    fun logIn(username: String, password: String, captcha: String? = null) {
        if (isNetworkAvailable()) {
            if (username.isEmpty() || password.isEmpty())
                message.value = getResourceString(R.string.some_field_is_empty_fill_all_fields)
            else {
                if (captcha != null && captcha.isBlank()) return message.postValue(
                    getResourceString(
                        R.string.some_field_is_empty_fill_all_fields
                    )
                )
                authorize(username, password, captcha)
            }
        } else message.value =
            getResourceString(R.string.for_authorize_you_need_network_connection)
    }

    private fun authorize(username: String, password: String, code: String? = null) {
        val requestData = mutableMapOf<String, String>()
        requestData[USERNAME] = username
        requestData[PASSWORD] = password

        if (code != null) {
            requestData[Constants.CODE] = code
            requestData.putAll(App.cookies)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result =
                NetworkHandler.getDocument(
                    NetworkHandler.LOGIN_URL,
                    NetworkMethod.POST,
                    requestData
                )
            when (result) {
                is Result.Success -> {
                    val parsedDoc = result.data.parse()
                    if (parsedDoc.location() == NetworkHandler.LOGIN_LOCATION) {
                        val alertMessage = parsedDoc.select("span[class=alert-message]").text()
                        if (alertMessage.contains("დამცავი", false)) {
                            getCaptcha()
                            message.postValue(getResourceString(R.string.you_have_to_enter_captcha))
                        }
                        message.postValue(getResourceString(R.string.given_information_is_incorrect))
                    } else {
                        App.cookies = result.data.cookies()

                        if(firstTimeUser) addUser(username, password)
                        else auth.postValue(true)
                    }
                }
                is Result.Error -> message.postValue(result.exception.message)
            }
        }
    }

    private fun getCaptcha() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                NetworkHandler.getDocument(NetworkHandler.CAPTCHA_URL, NetworkMethod.GET)) {
                is Result.Success -> {
                    App.cookies = result.data.cookies()
                    captcha.postValue(result.data.bodyAsBytes().inputStream().readBytes())
                }
                else -> message.postValue("Error while getting captcha")
            }
        }
    }

    private fun addUser(username: String, password: String) {
        SharedPreferencesHandler.writeStringSP(USERNAME, username)
        SharedPreferencesHandler.writeStringSP(PASSWORD, password)
        viewModelScope.launch(Dispatchers.IO) {
            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_fullname))
            val fullname = NetworkHandler.getUserFullname()

            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_credits))
            val userCredits = NetworkHandler.getUserCredits()

            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_rating))
            val userRating = NetworkHandler.getUserRating()

            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_image))
            val userImage = NetworkHandler.getUserImage()

            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_lectures))
            val lectures = NetworkHandler.getLectures()

            App.appDatabase.clearAllTables()
            val user = User(
                fullName = fullname,
                userCredits = userCredits,
                userRating = userRating,
                userImage = userImage,
                lectures = lectures
            )
            App.appDatabase.userDao().insertAll(user)
            auth.postValue(true)
        }
    }
}