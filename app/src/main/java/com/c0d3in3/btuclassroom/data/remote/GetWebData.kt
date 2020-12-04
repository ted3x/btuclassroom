package com.c0d3in3.btuclassroom.data.remote

import android.content.Context
import android.os.AsyncTask
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import android.graphics.BitmapFactory
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import com.c0d3in3.btuclassroom.DatabaseHelper
import com.c0d3in3.btuclassroom.writeLongCache
import org.jetbrains.anko.db.*


class GetWebData{

    lateinit var loginDoc : Connection.Response
    lateinit var dataDoc : Connection.Response
    lateinit var parsedData : Document

    fun getUserCredits(context: Context, cookies: String){
        class DemoTask: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                val jsonObject = JSONObject(cookies)
                val outputMap = HashMap<String, String>()
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject.get(key) as String
                    outputMap[key] = value
                }
                dataDoc = Jsoup.connect("https://classroom.btu.edu.ge/ge/student/me/index").cookies(outputMap).method(Connection.Method.GET).execute()


                return null
            }

            override fun onPostExecute(result: Void?) {
                parsedData = dataDoc.parse()
                val userCreditsString = parsedData.select("ul[class=inline]")[0].select("li").text()
                val userCredits = userCreditsString.replace("[^0-9]".toRegex(), "").toLong()
                writeLongCache(
                    context,
                    "userCredits",
                    userCredits
                )
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    fun getUserRating(context: Context, cookies: String){
        class DemoTask: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                val jsonObject = JSONObject(cookies)
                val outputMap = HashMap<String, String>()
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject.get(key) as String
                    outputMap[key] = value
                }
                dataDoc = Jsoup.connect("https://classroom.btu.edu.ge/ge/student/rating").cookies(outputMap).method(Connection.Method.GET).execute()


                return null
            }

            override fun onPostExecute(result: Void?) {
                parsedData = dataDoc.parse()
                val userRatingString = parsedData.select("div[class=col-md-18]").text()
                val userRating = userRatingString.replace("[^0-9]".toRegex(), "")
                if(userRating.length < 2){
                    writeLongCache(
                        context,
                        "userRating",
                        userRating.substring(0, 1).toLong()
                    )
                }
                else {
                    writeLongCache(
                        context,
                        "userRating",
                        userRating.substring(0, 2).toLong()
                    )
                }
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    fun getUserImage(context: Context, cookies: String){
        lateinit var userImageDoc : Connection.Response
        class DemoTask: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                val jsonObject = JSONObject(cookies)
                val outputMap = HashMap<String, String>()
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject.get(key) as String
                    outputMap[key] = value
                }
                dataDoc = Jsoup.connect("https://classroom.btu.edu.ge/ge/student/resume/personal").cookies(outputMap).method(Connection.Method.GET).execute()
                parsedData = dataDoc.parse()
                val userImageSrc = parsedData.select("img[class=img-responsive img-thumbnail user-photo]").attr("src")
                userImageDoc = Jsoup.connect(userImageSrc)
                    // and other hidden fields which are being passed in post request.
                    .userAgent("Mozilla")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()

                return null
            }

            override fun onPostExecute(result: Void?) {
                val bitmaps = BitmapFactory.decodeStream(userImageDoc.bodyAsBytes().inputStream())
                insertImg(context, bitmaps)
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    fun insertImg(context: Context, img: Bitmap){


        val data = getBitmapAsByteArray(img) // this is a function
        val db = context.database.writableDatabase

        db.createTable(
            "userInfo", true,
            "_userImage" to BLOB
        )

        db.insert(
            "userInfo", "_userImage" to data
        )

        db.close()

    }

    private fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }



}







