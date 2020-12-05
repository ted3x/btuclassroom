package com.c0d3in3.btuclassroom

import android.app.Application
import com.c0d3in3.btuclassroom.data.local.AppDatabase
import com.c0d3in3.btuclassroom.data.local.schedule.LectureRepository
import com.c0d3in3.btuclassroom.data.local.user.UserRepository

class App : Application() {

    companion object{
        lateinit var instance : App
        lateinit var appDatabase: AppDatabase
        lateinit var cookies: Map<String, String>
        lateinit var userRepository: UserRepository
        lateinit var lectureRepository: LectureRepository
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        appDatabase = AppDatabase.build(applicationContext)
        userRepository =
            UserRepository()
        lectureRepository = LectureRepository()
    }
}