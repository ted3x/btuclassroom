package com.c0d3in3.btuclassroom.shared_preferences

import android.content.Context.MODE_PRIVATE
import com.c0d3in3.btuclassroom.App

object SharedPreferencesHandler {

    private const val SHARED_PREFERENCES_FILE_KEY = "BTU-CLASSROOM-PREFERENCES"

    private val sharedPreferences = App.instance.applicationContext.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    fun writeString(key : String, value : String){
        editor.putString(key, value).apply()
    }

    fun writeLong(key : String, value : Long){
        editor.putLong(key, value).apply()
    }

    fun writeFloat(key : String, value : Float){
        editor.putFloat(key, value).apply()
    }

    fun writeBoolean(key : String, value : Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun getString(key : String) = sharedPreferences.getString(key, "").toString()

    fun getLong(key : String) = sharedPreferences.getLong(key, 0)

    fun getFloat(key : String) = sharedPreferences.getFloat(key, 0.0F)

    fun getBoolean(key : String) = sharedPreferences.getBoolean(key, false)

    fun remove(key: String){
        editor.remove(key).apply()
    }
}