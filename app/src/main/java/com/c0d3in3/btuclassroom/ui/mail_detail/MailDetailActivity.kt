package com.c0d3in3.btuclassroom.ui.mail_detail

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c0d3in3.btuclassroom.ui.mail.MailListActivity
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.getStringCache
import kotlinx.android.synthetic.main.activity_detail_mail.*
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.HashMap

class MailDetailActivity : AppCompatActivity() {


    lateinit var dataDoc : Connection.Response
    lateinit var parsedData : Document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mail)

        init()
    }

    private fun init(){
        sendRequest(
            getStringCache(this, "mailURL"),
            getStringCache(this, "cookies")
        )
    }


    override fun onBackPressed(){
        val intent = Intent(this, MailListActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun sendRequest(url: String,cookies: String) {
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
                dataDoc = Jsoup.connect(url).cookies(outputMap).method(Connection.Method.GET).execute()
                parsedData = dataDoc.parse()

                return null
            }

            override fun onPostExecute(result: Void?) {
                var mAuthor = parsedData.select("legend").text()
                val mDate = parsedData.select("legend").select("date[class=pull-right]").text()
                val mTitle = parsedData.select("h3:not([class],[id])").text()
                val mMail = parsedData.select("div[id=message_body]").text()
                mAuthor = mAuthor.replace(Regex("[0-9]"), "")
                mAuthor = mAuthor.replace(Regex("-"), "")
                println(mAuthor)
                titleTextView.text = mTitle
                dateTextView.text = mDate
                mailTextView.text = mMail
                authorTextView.text = mAuthor
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }
}
