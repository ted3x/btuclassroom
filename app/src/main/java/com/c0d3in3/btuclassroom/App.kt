package com.c0d3in3.btuclassroom

import android.app.Application

class App : Application() {

    companion object{
        lateinit var instance : App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}