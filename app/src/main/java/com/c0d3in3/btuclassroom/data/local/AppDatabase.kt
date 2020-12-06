package com.c0d3in3.btuclassroom.data.local

import android.content.Context
import androidx.room.*
import com.c0d3in3.btuclassroom.data.local.user.User
import com.c0d3in3.btuclassroom.data.local.user.UserDao
import com.c0d3in3.btuclassroom.model.Lecture

@Database(entities = [User::class], version = 1)
@TypeConverters(LectureConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object{
        private const val DATABASE_NAME = "BTUClassroom-DB"
        const val DATABASE_VERSION = 1

        fun build(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
    abstract fun userDao(): UserDao
}