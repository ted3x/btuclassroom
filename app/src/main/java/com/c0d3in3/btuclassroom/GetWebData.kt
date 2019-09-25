package com.c0d3in3.btuclassroom

import android.content.Context
import android.os.AsyncTask
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import android.R.attr.data




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
                parsedData = dataDoc.parse()
                val userCreditsString = parsedData.select("ul[class=inline]")[0].select("li").text()
                val userCredits = userCreditsString.replace("[^0-9]".toRegex(), "").toLong()
                writeLongCache(context, "userCredits", userCredits)

                return null
            }

            override fun onPostExecute(result: Void?) {
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
                parsedData = dataDoc.parse()
                val userRatingString = parsedData.select("div[class=col-md-18]").text()
                val userRating = userRatingString.replace("[^0-9]".toRegex(), "")
                writeLongCache(context, "userRating", userRating.substring(0,2).toLong())

                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

}







