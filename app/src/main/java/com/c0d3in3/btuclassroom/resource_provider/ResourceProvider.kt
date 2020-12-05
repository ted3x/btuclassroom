package com.c0d3in3.btuclassroom.resource_provider

import com.c0d3in3.btuclassroom.App

object ResourceProvider {

    fun getResourceString(stringId: Int) = App.instance.applicationContext.getString(stringId)

}