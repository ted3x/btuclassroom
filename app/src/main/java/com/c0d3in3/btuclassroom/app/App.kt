package com.c0d3in3.btuclassroom.app

import android.app.Application
import com.c0d3in3.btuclassroom.data.local.user.User

class App : Application() {

    companion object{
        lateinit var appComponent: AppComponent
        lateinit var cookies: Map<String, String>
        lateinit var instance : Application
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        instance = this
    }

    private fun initDagger(app: Application): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}