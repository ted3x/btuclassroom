package com.c0d3in3.btuclassroom.shared_preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.c0d3in3.btuclassroom.utils.Constants
import com.c0d3in3.btuclassroom.ui.login.LoginViewModel
import javax.inject.Inject

class SharedPreferencesHandler @Inject constructor(context: Context) {

    companion object{
        private const val SHARED_PREFERENCES_FILE_KEY = "BTU-CLASSROOM-PREFERENCES"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    fun writeStringSP(key : String, value : String){
        editor.putString(key, value).apply()
    }

    fun writeLongSP(key : String, value : Long){
        editor.putLong(key, value).apply()
    }

    fun writeFloatSP(key : String, value : Float){
        editor.putFloat(key, value).apply()
    }

    fun writeBooleanSP(key : String, value : Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getStringSP(key : String) = sharedPreferences.getString(key, "").toString()

    fun getLongSP(key : String) = sharedPreferences.getLong(key, 0)

    fun getFloatSP(key : String) = sharedPreferences.getFloat(key, 0.0F)

    fun getBooleanSP(key : String) = sharedPreferences.getBoolean(key, false)

    fun removeSP(key: String){
        editor.remove(key).apply()
    }

    fun writeUserData(username: String, password: String){
        writeStringSP(Constants.USERNAME, username)
        writeStringSP(Constants.PASSWORD, password)
        writeBooleanSP(LoginViewModel.REGISTERED_USER, true)
    }
}