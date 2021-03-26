package com.c0d3in3.btuclassroom.model

data class Lecture(
    val day: Int?,
    val room: String?,
    val startTime: String?,
    val endTime: String?,
    val lecture: String?,
    val group: String?,
    val lecturer: String?
){

    val shortLectureName : String
    get() {
        val name = lecture?.split("- ")
        return name?.get(1) ?: ""
    }
}