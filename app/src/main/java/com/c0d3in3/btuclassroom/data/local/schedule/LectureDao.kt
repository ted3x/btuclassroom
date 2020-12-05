package com.c0d3in3.btuclassroom.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LectureDao {

    @Query("SELECT * FROM lecture")
    fun getAll(): List<Lecture>

    @Insert
    fun insert(vararg lectures: Lecture)

}