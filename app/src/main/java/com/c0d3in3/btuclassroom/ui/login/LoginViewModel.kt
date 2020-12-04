package com.c0d3in3.btuclassroom.ui.login

import androidx.lifecycle.ViewModel
import com.c0d3in3.btuclassroom.Constants.COOKIES
import com.c0d3in3.btuclassroom.Constants.USERNAME
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler

class LoginViewModel : ViewModel() {

    private lateinit var username : String

    init {
        SharedPreferencesHandler.remove(COOKIES)
        username = SharedPreferencesHandler.getString(USERNAME)
        logIn()
    }

    private fun logIn(){

        if (username.isNotEmpty()) {
            if(networkAvaliable(this)){
                setContentView(R.layout.loading)
                autoLogin = true
                Timer().schedule(3000) {
                    sendRequest(
                        getStringCache(
                            this@LoginActivity,
                            "username"
                        ),
                        getStringCache(
                            this@LoginActivity,
                            "password"
                        ), null)
                }
            }
            else{
                setContentView(R.layout.loading)
                loadingTextView.text = "მიმდინარეობს cache მეხსიერების ჩატვირთვა!\n(offline mode)"
                Timer().schedule(3000) {
                    makeOfflineLogIn()
                }
            }
        }
        else {
            setContentView(R.layout.activity_login)
            init()
        }
    }
}
