package com.c0d3in3.btuclassroom.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    val id: Int,
    val day: Int,
    val room: String,
    val time: String,
    val lecture: String,
    val group: String,
    val lecturer: String
) : Parcelable