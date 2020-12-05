package com.c0d3in3.btuclassroom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.local.user.UserDao
import com.c0d3in3.btuclassroom.data.local.schedule.Lecture
import com.c0d3in3.btuclassroom.data.local.schedule.LectureDao

@Database(entities = [User::class, Lecture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object{
        private const val DATABASE_NAME = "BTUClassroom-DB"
        const val DATABASE_VERSION = 1

        fun build(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
    abstract fun userDao(): UserDao
    abstract fun lecture() : LectureDao
}