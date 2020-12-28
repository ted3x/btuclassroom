package com.c0d3in3.btuclassroom.data.remote

import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.model.Course
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.data.remote.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider
import com.c0d3in3.btuclassroom.utils.getDayInt
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class NetworkHandler @Inject constructor(private val resourceProvider: ResourceProvider){

    companion object{
        private const val BASE_URL = "https://classroom.btu.edu.ge/ge/"
        const val CAPTCHA_URL = "${BASE_URL}login/captcha"
        const val LOGIN_URL = "${BASE_URL}login/trylogin"
        private const val RATING_URL = "${BASE_URL}student/rating"
        private const val CREDITS_URL = "${BASE_URL}student/me/index"
        private const val IMAGE_URL = "${BASE_URL}student/resume/personal"
        const val LOGIN_LOCATION = "${BASE_URL}login"
        const val MAIL_URL = "${BASE_URL}messages"
        private const val SCHEDULE_URL = "${BASE_URL}student/me/schedule"
        private const val COURSES_URL = "${BASE_URL}student/me/courses"
    }

    //Data is always represented as Map<String, String>
    suspend fun getDocument(
        url: String,
        method: NetworkMethod,
        data: Map<String, String>? = null,
        includeCookies: Boolean = false
    ): Result<Connection.Response> {
        if (!isNetworkAvailable()) return Result.Error(null, "ინტერნეტთან წვდომა არ გვაქვს : (")
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
        return withContext(Dispatchers.IO) {
            when (val result = getDocument(CREDITS_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    val parsedData = result.data.parse()
                    if (parsedData != null) {
                        val creditsText =
                            parsedData.select("ul[class=inline]")[0].select("li").text()
                        userCredits = creditsText.replace("[^0-9]".toRegex(), "").toLong()
                    }
                }
            }
            userCredits
        }
    }

    suspend fun getUserImage(): ByteArray? {
        var imageBytes: ByteArray? = null
        return withContext(Dispatchers.IO) {
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
            imageBytes
        }
    }

    suspend fun getUserFullName(): String {
        return withContext(Dispatchers.IO) {
            val userFullName =
                when (val result = getDocument(IMAGE_URL, NetworkMethod.GET, null, true)) {
                    is Result.Success -> {
                        val parsedData = result.data.parse()
                        val urlStr =
                            parsedData.select("a[href=\"https://classroom.btu.edu.ge/ge/profile/index\"]")
                        urlStr.text().toString()
                    }
                    else -> ""
                }
            userFullName
        }
    }

    suspend fun getLectures(): List<Lecture> {
        val lecturesList = mutableListOf<Lecture>()
        return withContext(Dispatchers.IO) {
            when (val result =
                getDocument(SCHEDULE_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    var day = ""
                    val parsedDoc = result.data.parse()
                    for (tag in parsedDoc.allElements) {
                        if (tag.className() == "info") {
                            day = tag.text()
                            tag.remove()
                        }
                        if (tag.className() == "tip") {
                            val scheduleValues = ArrayList<String>()
                            for (lecture in 0 until tag.select("td").size - 1) {
                                scheduleValues.add(tag.select("td")[lecture].text())
                            }
                            val lectureTime = scheduleValues[0].split(" - ")
                            val lecture = Lecture(
                                day = getDayInt(day),
                                startTime = lectureTime[0],
                                endTime = lectureTime[1],
                                room = scheduleValues[1],
                                lecture = scheduleValues[2],
                                group = scheduleValues[3],
                                lecturer = scheduleValues[4]
                            )
                            lecturesList.add(lecture)
                            tag.remove()
                        }
                        if (tag.className() == "info" && tag.text() != day) continue
                    }
                }
                is Result.Error -> resourceProvider.getResourceString(R.string.we_couldnt_get_your_lectures_try_again)
            }
            lecturesList
        }
    }

    suspend fun getCourses(): List<Course> {
        val coursesList = mutableListOf<Course>()
        return withContext(Dispatchers.IO) {
            when (val result =
                getDocument(COURSES_URL, NetworkMethod.GET, null, true)) {
                is Result.Success -> {
                    val parsedDoc = result.data.parse()
                    val coursesBody = parsedDoc.getElementsByTag("tbody").select("tr")
                    coursesBody.removeAt(coursesBody.size-1)
                    coursesBody.forEach {
                        val courseValues = it.select("td").toList()
                        //TODO COURSEID
//                            val courseUrl = courseValues[2]
//                            val courseId = courseUrl.split("/")
                        val courseCredits = courseValues[5].text().toInt()
                        val coursePoints = courseValues[3].text().toInt()
                        val courseName = courseValues[2].text()
                        val course = Course(
                            courseId = 0,
                            courseCredit = courseCredits,
                            courseName = courseName,
                            coursePoint = coursePoints
                        )
                        coursesList.add(course)
                    }
                }
                is Result.Error -> resourceProvider.getResourceString(R.string.we_couldnt_get_your_lectures_try_again)
            }
            coursesList
        }
    }
}