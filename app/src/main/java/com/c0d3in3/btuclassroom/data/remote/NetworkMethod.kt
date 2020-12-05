package com.c0d3in3.btuclassroom.data.remote

import org.jsoup.Connection

enum class NetworkMethod(val methodType: Connection.Method) {
    GET(Connection.Method.GET),
    POST(Connection.Method.POST)
}