package com.c0d3in3.btuclassroom.resource_provider

import android.annotation.SuppressLint
import com.c0d3in3.btuclassroom.App

object ResourceProvider {

    fun getResourceString(stringId: Int) = App.instance.applicationContext.getString(stringId)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(drawableId: Int) = App.instance.applicationContext.getDrawable(drawableId)
}