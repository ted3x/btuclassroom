package com.c0d3in3.btuclassroom.ui.login

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.Constants
import com.c0d3in3.btuclassroom.Constants.COOKIES
import com.c0d3in3.btuclassroom.Constants.PASSWORD
import com.c0d3in3.btuclassroom.Constants.USERNAME
import com.c0d3in3.btuclassroom.R
import ge.ted3x.core.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider.getResourceString
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class LoginViewModel : ge.ted3x.core.base.BaseViewModel() {

    companion object {
        const val REGISTERED_USER = "registered_user"
        const val AUTO_LOGIN_DELAY = 2500L
    }

    private val username: String
    private val password: String
    val captcha = MutableLiveData<ByteArray>()
    val auth = MutableLiveData<Boolean>()
    val loadingMessage = MutableLiveData<String>()
    private val registeredUser : Boolean

    init {
        SharedPreferencesHandler.removeSP(COOKIES)
        username = SharedPreferencesHandler.getStringSP(USERNAME)
        password = SharedPreferencesHandler.getStringSP(PASSWORD)
        registeredUser = SharedPreferencesHandler.getBooleanSP(REGISTERED_USER)
        if (username.isNotBlank() && password.isNotBlank()) {
            if(isNetworkAvailable()) autoLogIn(username, password)
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

                        if(registeredUser){
                            App.currentUser = App.userRepository.getUser()
                            auth.postValue(true)
                        }
                        else addUser(username, password)
                    }
                }
                is Result.Error -> message.postValue(result.message)
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
                is Result.Error -> message.postValue(result.message ?: "Error while getting captcha")
            }
        }
    }

    private fun autoLogIn(username: String, password: String) {
        loadingMessage.value = getResourceString(R.string.auto_log_in)
        viewModelScope.launch {
            delay(AUTO_LOGIN_DELAY)
            logIn(username, password)
        }
    }

    private fun addUser(username: String, password: String) {
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

            loadingMessage.postValue(getResourceString(R.string.we_are_getting_your_corses))
            val courses = NetworkHandler.getCourses()

            App.appDatabase.clearAllTables()
            val user = User(
                fullName = fullname,
                userCredits = userCredits,
                userRating = userRating,
                userImage = userImage,
                lectures = lectures,
                courses = courses
            )
            App.appDatabase.userDao().insertAll(user)
            App.currentUser = user

            SharedPreferencesHandler.writeStringSP(USERNAME, username)
            SharedPreferencesHandler.writeStringSP(PASSWORD, password)
            SharedPreferencesHandler.writeBooleanSP(REGISTERED_USER, true)

            auth.postValue(true)
        }
    }
}