package com.c0d3in3.btuclassroom

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.*
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import kotlin.collections.ArrayList
import android.os.Build
import androidx.core.app.NotificationCompat
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.Html
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.loading.*
import java.io.IOException
import java.sql.Blob


class MainActivity : AppCompatActivity() {

    private val INTERVAL = 1000 * 60 * 10
    lateinit var dataDoc : Connection.Response
    lateinit var parsedData : Document
    private var mailNumber = "0"



    //private var scheduleList = mutableListOf<Schedules>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        toggle.isDrawerIndicatorEnabled = true
        actionBar?.setDisplayHomeAsUpEnabled(true)
        init()

    }

    private fun init(){
        createNotificationChannel()
        val sharedPref: SharedPreferences = getSharedPreferences("MY_PREFS", 0)
        if(getBooleanCache(this, "firstTime")){
            createSchedule()
        }
        else{
            updateUI()
            userNameTexView.text = getStringCache(this, "userIdentity")
            startRepeatingTask()
        }
        if(networkAvaliable(this)) sendRequest("https://classroom.btu.edu.ge/ge/student/me/courses", getStringCache(this, "cookies"))
        logoutButton.setOnClickListener {
            sharedPref.edit().clear().apply()
            val db = database.writableDatabase
            db.dropTable("Schedule", true)
            db.dropTable("userInfo", true)
            stopRepeatingTask()
            db.close()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        mailButton.setOnClickListener {
            if(!networkAvaliable(this)){
                Toast.makeText(this, "წერილების სექციაზე გადასასვლელად საჭიროა ინტერნეტთან წვდომა. გთხოვთ შეამოწმოთ თქვენი კავშირი ინტერნეტთან!", Toast.LENGTH_LONG).show()
            }
            else{
                if(getStringCache(this, "cookies") == ""){
                    Toast.makeText(this, "offline კავშირის შემდეგ საიტთან დასაკავშირებლად საჭიროა ავტომატური ავტორიზაციის გავლა!",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    val intent = Intent(this, MailListActivity::class.java)
                    writeLongCache(this, "mailNumber", mailNumber.toLong())
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun sendRequest(url: String, cookies: String) {
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
                    displayError(this@MainActivity, "ვერ მოხერხდა საიტთან დაკავშირება! სცადეთ თავიდან.")
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                parsedData = dataDoc.parse()
                if(getBooleanCache(this@MainActivity, "firstTime")){
                    val urlStr = parsedData.select("a[href=\"https://classroom.btu.edu.ge/ge/profile/index\"]")
                    userNameTexView.text = urlStr.text().toString()
                    writeStringCache(this@MainActivity, "userIdentity", urlStr.text().toString())
                    writeBooleanCache(this@MainActivity, "firstTime", false)
                    setUserCredits()
                    //ratingTextView.text = getLongCache(this@MainActivity, "userRating").toString()

                    val data = UserInfo(this@MainActivity).getInfo()
                    val img = data[0]._userImage
                    val bitmaps = BitmapFactory.decodeByteArray(img, 0, img.size)
                    userLogoImageView.setImageBitmap(bitmaps)

                    val calendar = Calendar.getInstance()
                    val curYear = calendar.get(Calendar.YEAR)

                    yearTextView.text = curYear.toString() + " - " + (calendar.get(Calendar.YEAR) +1).toString()

                    BTUTextView.text = Html.fromHtml("<b>" + "BTU" + "</b>" + " <font color='#000'>Classroom")

                }
                getMail()

                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    fun setUserCredits(){
        val userCredits = getLongCache(this@MainActivity, "userCredits")
        if(userCredits < 60){
            semestriTextView.text = "I კურსი"
        }
        if(userCredits in 60..119){
            semestriTextView.text = "II კურსი"
        }
        if(userCredits in 120..179){
            semestriTextView.text = "III კურსი"
        }
        if(userCredits in 180..240){
            semestriTextView.text = "IV კურსი"
        }
    }

    private fun getMail(){
        mailNumber = parsedData.select("a[href=\"https://classroom.btu.edu.ge/ge/messages\"]").select("span").text()
        if(getLongCache(this, "mailNumber") < mailNumber.toLong()){
            val unreadDrawable = resources.getDrawable( R.drawable.ic_mails_unread )
            mailButton.setCompoundDrawablesWithIntrinsicBounds(unreadDrawable,null, null, null)
        }
    }

    private fun createSchedule() {
        val db = database.writableDatabase
        db.createTable(
            "Schedule", true,
            "_id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "_day" to INTEGER,
            "_room" to TEXT,
            "_time" to TEXT,
            "_lecture" to TEXT,
            "_group" to TEXT,
            "_lecturer" to TEXT
        )

        getSchedule(
            "https://classroom.btu.edu.ge/ge/student/me/schedule",
            getStringCache(this, "cookies")
        )
        db.close()
    }

    private fun getSchedule(url: String, cookies: String) {
        lateinit var scheduleResponse : Connection.Response
        lateinit var scheduleData : Document
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
                scheduleResponse = Jsoup.connect(url).cookies(outputMap).method(Connection.Method.GET).execute()
                scheduleData = scheduleResponse.parse()
                return null
            }

            override fun onPostExecute(result: Void?) {
                insertData(scheduleData)
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    fun insertData(data : Document) {
        var day = ""
        val db = database.writableDatabase
        for (tag in data.allElements) {
            if (tag.className() == "info") {
                //println(tag.text())
                day = tag.text()
                tag.remove()
            }
            if (tag.className() == "tip") {
                var values = ArrayList<String>()
                for (lecture in 0 until tag.select("td").size - 1) {
                    //println(tag.select("td")[lecture].text())
                    values.add(tag.select("td")[lecture].text())
                }
                db.insert(
                    "Schedule", "_day" to getDayInt(day), "_time" to values[0], "_room" to values[1],
                    "_lecture" to values[2], "_group" to values[3], "_lecturer" to values[4]
                )
                tag.remove()
            }
            if (tag.className() == "info" && tag.text() != day) {
                continue
            }
        }
        updateNextLectureUI()
        db.close()
    }

    fun updateNextLectureUI(){
        val schedule = Schedule(this).getSchedule()
        val lecture = getClosestLecture(this)
        nextDayTextView.text = getDayString(schedule[lecture]._day)
        lectureRoomTextView.text = schedule[lecture]._room
        lecturerTextView.text = Html.fromHtml("<font color='#cccccc'>ლექტორი:<br>" + "<font color='#666666'>" + schedule[lecture]._lecturer)
        lectureNameTextView.text = schedule[lecture]._lecture.substring(9)
        lectureTimeTextView.text = schedule[lecture]._time.substring(0,5)

    }

    private fun notifyUser(){
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val schedule = Schedule(this).getSchedule()
        val lecture = getClosestLecture(this)
        val calendar = Calendar.getInstance()
        if(schedule[lecture]._time.substring(0,2).toInt()-1 == calendar.get(Calendar.HOUR_OF_DAY)){
            var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("შემდეგი ლექცია იწყება.")
                .setContentText("${schedule[lecture]._lecture.substring(9)} იწყება ${60-calendar.get(Calendar.MINUTE)} წუთში. ოთახი ${schedule[lecture]._room}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText("${schedule[lecture]._lecture.substring(9)} იწყება. ოთახი ${schedule[lecture]._room}"))


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, builder.build())
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MAIN_CHANNEL"
            val descriptionText = "Lecture Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    var mHandler = Handler()

    private var mHandlerTask: Runnable = object : Runnable {
        override fun run() {
            notifyUser()
            mHandler.postDelayed(this, INTERVAL.toLong())
        }
    }

    private fun startRepeatingTask() {
        mHandlerTask.run()
    }

    private fun stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask)
    }


    private fun updateUI(){
        setUserCredits()
        updateNextLectureUI()

        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)

        yearTextView.text = curYear.toString() + " - " + (calendar.get(Calendar.YEAR) +1).toString()

        BTUTextView.text = Html.fromHtml("<b>" + "BTU" + "</b>" + " <font color='#000'>Classroom")

        val data = UserInfo(this).getInfo()
        val img = data[0]._userImage
        val bitmaps = BitmapFactory.decodeByteArray(img, 0, img.size)
        userLogoImageView.setImageBitmap(bitmaps)

        //ratingTextView.text = getLongCache(this, "userRating").toString()
    }

}

