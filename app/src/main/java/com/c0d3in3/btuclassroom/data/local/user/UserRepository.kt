package com.c0d3in3.btuclassroom.data.local.user

import javax.inject.Inject
import javax.inject.Singleton

class UserRepository @Inject constructor(private val userDao : UserDao) {

    suspend fun getUser() = userDao.getUser()

    fun addUser(user: User) = userDao.insertAll(user)

    fun dropTable() = userDao.dropTable()

}