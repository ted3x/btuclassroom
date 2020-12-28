package com.c0d3in3.btuclassroom.resource_provider

import android.annotation.SuppressLint
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

class ResourceProvider @Inject constructor(private val context: Context) {

    fun getResourceString(stringId: Int) = context.getString(stringId)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(drawableId: Int) = context.getDrawable(drawableId)
}