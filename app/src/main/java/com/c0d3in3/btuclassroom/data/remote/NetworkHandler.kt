package com.c0d3in3.btuclassroom.data.remote

import android.graphics.Bitmap
import com.c0d3in3.btuclassroom.App
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.ByteArrayOutputStream
import com.c0d3in3.btuclassroom.model.Result
import kotlinx.coroutines.*
import java.io.IOException

object NetworkHandler {

    const val CAPTCHA_URL = "https://classroom.btu.edu.ge/ge/login/captcha"
    const val LOGIN_URL = "https://classroom.btu.edu.ge/ge/login/trylogin"
    private const val RATING_URL = "https://classroom.btu.edu.ge/ge/student/rating"
    private const val CREDITS_URL = "https://classroom.btu.edu.ge/ge/student/me/index"
    private const val IMAGE_URL = "https://classroom.btu.edu.ge/ge/student/resume/personal"
    const val LOGIN_LOCATION = "https://classroom.btu.edu.ge/ge/login"
    const val MAIL_URL = "https://classroom.btu.edu.ge/ge/messages"
    const val SCHEDULE_URL = "https://classroom.btu.edu.ge/ge/student/me/schedule"
    const val DASHBOARD_URL = "https://classroom.btu.edu.ge/ge/student/me/courses"

    //Data is always represented as Map<String, String>
    suspend fun getDocument(
        url: String,
        method: NetworkMethod,
        data: Map<String, String>? = null,
        includeCookies: Boolean = false
    ): Result<Connection.Response> {
        val doc = Jsoup.connect(url)
            .method(method.methodType)
            .userAgent("Mozilla")
        if (data != null) doc.data(data)
        if (includeCookies) doc.cookies(App.cookies)
        return try {

            withContext(Dispatchers.IO) {
                val response = doc.execute()
                Result.Success(response)
            }
        } catch (e: IOException) {
            Result.Error(e)
        }
    }


    suspend fun getUserRating(): Long {
        var userRating: Long = 0
        val scope = CoroutineScope(Dispatchers.IO).async {
            when (val result = getDocument(RATING_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    val parsedData = result.data.parse()
                    val userRatingString =
                        parsedData.select("div[class=col-md-18]").text()
                            .replace("[^0-9]".toRegex(), "")
                    userRating = if (userRatingString.length < 2)
                        userRatingString.substring(0, 1).toLong()
                    else userRatingString.substring(0, 2).toLong()
                }
                is Result.Error -> 0
            }
        }
        scope.await()
        return userRating
    }

    suspend fun getUserCredits(): Long {
        var userCredits: Long = 0
        val scope = CoroutineScope(Dispatchers.IO).async {
            when (val result = getDocument(CREDITS_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    val parsedData = result.data.parse()
                    if (parsedData != null) {
                        val creditsText =
                            parsedData.select("ul[class=inline]")[0].select("li").text()
                        userCredits = creditsText.replace("[^0-9]".toRegex(), "").toLong()
                    }
                }
                else -> userCredits = 0
            }
        }
        scope.await()
        return userCredits
    }

    suspend fun getUserImage(): ByteArray? {
        var imageBytes: ByteArray? = null
        val scope = CoroutineScope(Dispatchers.IO).async {
            when (val result = getDocument(IMAGE_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    val userImageSrc =
                        result.data.parse()
                            .select("img[class=img-responsive img-thumbnail user-photo]")
                            .attr("src")
                    val userImageDoc = Jsoup.connect(userImageSrc)
                        .userAgent("Mozilla")
                        .method(Connection.Method.GET)
                        .ignoreContentType(true)
                        .execute()
                    imageBytes = userImageDoc.bodyAsBytes()
                }
            }
        }
        scope.await()
        return imageBytes
    }

    suspend fun getUserFullname(): String {
        var userFullname = ""
        val scope = CoroutineScope(Dispatchers.IO).async {
            userFullname =
                when (val result = getDocument(IMAGE_URL, NetworkMethod.GET, null, true)) {
                    is Result.Success -> {
                        val parsedData = result.data.parse()
                        val urlStr =
                            parsedData.select("a[href=\"https://classroom.btu.edu.ge/ge/profile/index\"]")
                        urlStr.text().toString()
                    }
                    else -> ""
                }
        }
        scope.await()
        return userFullname
    }

    private fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

}