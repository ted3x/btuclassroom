package com.c0d3in3.btuclassroom.data.local.user

import com.c0d3in3.btuclassroom.App

class UserRepository {

    private val userDao = App.appDatabase.userDao()

    fun getUser() = userDao.getUser()
}