package com.c0d3in3.btuclassroom.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.utils.Constants
import com.c0d3in3.btuclassroom.utils.Constants.COOKIES
import com.c0d3in3.btuclassroom.utils.Constants.PASSWORD
import com.c0d3in3.btuclassroom.utils.Constants.USERNAME
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.local.user.UserRepository
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.data.remote.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val sharedPreferencesHandler: SharedPreferencesHandler,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider,
    private val networkHandler: NetworkHandler
) : BaseViewModel() {

    companion object {
        const val REGISTERED_USER = "registered_user"
        const val AUTO_LOGIN_DELAY = 2500L
    }

    private val username: String
    private val password: String
    val captcha = MutableLiveData<ByteArray>()
    val auth = MutableLiveData<Boolean>()
    val loadingMessage = MutableLiveData<String>()
    private val isAlreadyRegistered: Boolean

    init {
        sharedPreferencesHandler.removeSP(COOKIES)
        username = sharedPreferencesHandler.getStringSP(USERNAME)
        password = sharedPreferencesHandler.getStringSP(PASSWORD)
        isAlreadyRegistered = sharedPreferencesHandler.getBooleanSP(REGISTERED_USER)
        if (username.isNotBlank() && password.isNotBlank()) {
            if (isNetworkAvailable()) autoLogIn(username, password)
            else auth.value = true
        }
    }

    fun logIn(username: String, password: String, captcha: String? = null) {
        if (isNetworkAvailable()) {
            if (username.isEmpty() || password.isEmpty())
                message.value =
                    resourceProvider.getResourceString(R.string.some_field_is_empty_fill_all_fields)
            else {
                if (captcha != null && captcha.isBlank()) return message.postValue(
                    resourceProvider.getResourceString(
                        R.string.some_field_is_empty_fill_all_fields
                    )
                )
                authorize(username, password, captcha)
            }
        } else message.value =
            resourceProvider.getResourceString(R.string.for_authorize_you_need_network_connection)
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
                networkHandler.getDocument(
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
                            message.postValue(resourceProvider.getResourceString(R.string.you_have_to_enter_captcha))
                        }
                        message.postValue(resourceProvider.getResourceString(R.string.given_information_is_incorrect))
                    } else {
                        App.cookies = result.data.cookies()

                        if (isAlreadyRegistered)
                            auth.postValue(true)
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
                networkHandler.getDocument(NetworkHandler.CAPTCHA_URL, NetworkMethod.GET)) {
                is Result.Success -> {
                    App.cookies = result.data.cookies()
                    captcha.postValue(result.data.bodyAsBytes().inputStream().readBytes())
                }
                is Result.Error -> message.postValue(
                    result.message ?: "Error while getting captcha"
                )
            }
        }
    }

    private fun autoLogIn(username: String, password: String) {
        loadingMessage.value = resourceProvider.getResourceString(R.string.auto_log_in)
        viewModelScope.launch {
            delay(AUTO_LOGIN_DELAY)
            logIn(username, password)
        }
    }

    private fun addUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_fullname))
            val fullName = networkHandler.getUserFullName()

            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_credits))
            val userCredits = networkHandler.getUserCredits()

            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_rating))
            val userRating = networkHandler.getUserRating()

            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_image))
            val userImage = networkHandler.getUserImage()

            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_lectures))
            val lectures = networkHandler.getLectures()

            loadingMessage.postValue(resourceProvider.getResourceString(R.string.we_are_getting_your_corses))
            val courses = networkHandler.getCourses()

            userRepository.dropTable()
            val user = User(
                fullName = fullName,
                userCredits = userCredits,
                userRating = userRating,
                userImage = userImage,
                lectures = lectures,
                courses = courses
            )
            userRepository.addUser(user)
            sharedPreferencesHandler.writeUserData(username, password)

            auth.postValue(true)
        }
    }
}