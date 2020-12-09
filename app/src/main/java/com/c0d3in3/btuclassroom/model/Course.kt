package com.c0d3in3.btuclassroom.model

data class Course(
    val courseId : Int,
    val courseName : String,
    var coursePoint: Int,
    val courseCredit: Int
)