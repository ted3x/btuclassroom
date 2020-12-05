package com.c0d3in3.btuclassroom.data.local.schedule

import com.c0d3in3.btuclassroom.App

class LectureRepository {
    private val lectureDao = App.appDatabase.lecture()

    fun getLectures() = lectureDao.getAll()
    fun addLecture(lecture: Lecture) = lectureDao.insert(lecture)
}