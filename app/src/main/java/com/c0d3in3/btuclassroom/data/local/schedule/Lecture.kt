package com.c0d3in3.btuclassroom.data.local.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lecture(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "day") val day: Int?,
    @ColumnInfo(name = "room") val room: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "lecture") val lecture: String?,
    @ColumnInfo(name = "group") val group: String?,
    @ColumnInfo(name = "lecturer") val lecturer: String?
)