package com.c0d3in3.btuclassroom.resource_provider

import android.content.Context
import javax.inject.Inject

class ResourceProvider @Inject constructor(private val context: Context) {

    fun getResourceString(stringId: Int) = context.getString(stringId)
}