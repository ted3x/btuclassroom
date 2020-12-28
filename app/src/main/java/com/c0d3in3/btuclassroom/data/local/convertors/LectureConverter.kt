package com.c0d3in3.btuclassroom.data.local.convertors

import androidx.room.TypeConverter
import com.c0d3in3.btuclassroom.model.Lecture
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LectureConverter {
    @TypeConverter
    fun fromLectureList(lectureList: List<Lecture?>?): String? {
        if (lectureList == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Lecture?>?>() {}.type
        return Gson().toJson(lectureList, type)
    }

    @TypeConverter
    fun toLectureList(lectureString: String?): List<Lecture>? {
        if (lectureString == null) {
            return null
        }
        val type: Type = object : TypeToken<List<Lecture?>?>() {}.type
        return Gson().fromJson<List<Lecture>>(lectureString, type)
    }
}