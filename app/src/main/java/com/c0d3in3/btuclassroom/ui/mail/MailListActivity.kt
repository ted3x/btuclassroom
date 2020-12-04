package com.c0d3in3.btuclassroom.ui.mail

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.displayError
import com.c0d3in3.btuclassroom.ui.dashboard.DashboardActivity
import com.c0d3in3.btuclassroom.ui.mail.adapter.MailAdapter
import kotlinx.android.synthetic.main.activity_mail_list.*
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.HashMap

class MailListActivity : AppCompatActivity() {

    private var mailsList = mutableListOf<Mails>()

    private var count : Int = 0


    lateinit var dataDoc : Connection.Response
    lateinit var parsedData : Document
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)

        init()

        mailRecyclerView.layoutManager = LinearLayoutManager(this)

        mailRecyclerView.adapter =
            MailAdapter(mailsList, this)

    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    private fun init(){
        val sharedPref: SharedPreferences = getSharedPreferences("MY_PREFS", 0)
        sendRequest("https://classroom.btu.edu.ge/ge/messages", sharedPref.getString("cookies", "")!!)
    }


    fun sendRequest(url: String,cookies: String) {
        class DemoTask: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                val updateTask = this
                val jsonObject = JSONObject(cookies)
                val outputMap = HashMap<String, String>()
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject.get(key) as String
                    outputMap[key] = value
                }
                try{
                    dataDoc = Jsoup.connect(url).cookies(outputMap).method(Connection.Method.GET).execute()
                }
                catch (e: IOException){
                    updateTask.cancel(true)
                    displayError(
                        this@MailListActivity,
                        "ვერ მოხერხდა საიტთან დაკავშირება! სცადეთ თავიდან."
                    )
                }

                return null
            }

            override fun onPostExecute(result: Void?) {
                parsedData = dataDoc.parse()
                val main = parsedData.select("tr[class]")
                main.forEach{ele ->
                    var arg = ele.select("td")
                    if(ele.className().toString() == "info"){
                        mailsList.add(
                            Mails(
                                count,
                                arg[1].text(),
                                arg[2].text(),
                                arg[3].text(),
                                arg[1].select("a").attr("href").toString(),
                                true
                            )
                        )
                    }
                    else{
                        mailsList.add(
                            Mails(
                                count,
                                arg[1].text(),
                                arg[2].text(),
                                arg[3].text(),
                                arg[1].select("a").attr("href").toString(),
                                false
                            )
                        )
                    }
                    count += 1
                }
                if(count != 0){
                    mailRecyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    noMailsTextView.visibility = View.VISIBLE
                }
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    data class Mails(val id: Int, val author: String, val title: String, val date: String, val mailURL: String, val unread: Boolean)
}
