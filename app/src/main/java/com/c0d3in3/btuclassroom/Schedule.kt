package com.c0d3in3.btuclassroom

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select

import java.util.*

class Schedule(val context: Context) {
    fun getSchedule() : ArrayList<ScheduleData> = context.database.use {
        val schedules = ArrayList<ScheduleData>()

        select("Schedule", "_id", "_day", "_room", "_time", "_lecture", "_group", "_lecturer")
            .parseList(object: MapRowParser<List<ScheduleData>> {
                override fun parseRow(columns: Map<String, Any?>): List<ScheduleData> {
                    val id = columns.getValue("_id")
                    val day = columns.getValue("_day")
                    val room = columns.getValue("_room")
                    val time = columns.getValue("_time")
                    val lecture = columns.getValue("_lecture")
                    val group = columns.getValue("_group")
                    val lecturer = columns.getValue("_lecturer")

                    val schedule = ScheduleData(id.toString().toInt(), day.toString().toInt(), room.toString(),
                        time.toString(), lecture.toString(), group.toString(), lecturer.toString())

                    schedules.add(schedule)

                    return schedules
                }
            })

        schedules
    }
}

@Parcelize
data class ScheduleData(val _id: Int, val _day : Int, val _room : String, val _time : String,
                        val _lecture : String, val _group : String, val _lecturer : String): Parcelable{
}