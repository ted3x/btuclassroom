package com.c0d3in3.btuclassroom.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mail(
    val author: String,
    val title: String,
    val date: String,
    val url: String,
    var unread: Boolean,
    var mailText: String = ""
) : Parcelable