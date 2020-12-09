package com.c0d3in3.btuclassroom.data.local

import androidx.room.TypeConverter
import com.c0d3in3.btuclassroom.model.Course
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class CoursesConverter {
    @TypeConverter
    fun fromCoursesList(coursesList: List<Course?>?): String? {
        if (coursesList == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Course?>?>() {}.type
        return Gson().toJson(coursesList, type)
    }

    @TypeConverter
    fun toCoursesLit(courseString: String?): List<Course>? {
        if (courseString == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Course?>?>() {}.type
        return Gson().fromJson<List<Course>>(courseString, type)
    }
}