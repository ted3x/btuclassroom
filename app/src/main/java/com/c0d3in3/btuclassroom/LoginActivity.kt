package com.c0d3in3.btuclassroom

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import kotlin.concurrent.schedule
import android.os.StrictMode
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    lateinit var mCookies : MutableMap<String, String>
    var autoLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var PRIVATE_MODE = 0
        val MY_PREFS = "MY_PREFS"
        val sharedPref: SharedPreferences = getSharedPreferences(MY_PREFS, PRIVATE_MODE)
        val editor = sharedPref.edit()
        editor.remove("cookies")
        editor.apply()
        if (getStringCache(this@LoginActivity,"username").isNotEmpty()) {
            if(networkAvaliable(this)){
                setContentView(R.layout.loading)
                autoLogin = true
                Timer().schedule(3000) {
                    sendRequest(
                        getStringCache(this@LoginActivity, "username"),
                        getStringCache(this@LoginActivity,"password"), null)
                }
            }
            else{
                setContentView(R.layout.loading)
                loadingTextView.text = "მიმდინარეობს cache მეხსიერების ჩატვირთვა!\n(offline mode)"
                Timer().schedule(3000) {
                    makeOfflineLogIn()
                }
            }
        }
        else {
            setContentView(R.layout.activity_login)
            init()
        }
    }

    private fun init(){
        loginButton.setOnClickListener {

            if(networkAvaliable(this)){
                if(emailEditText.text.toString().isEmpty() or passwordEditText.text.toString().isEmpty()){
                    Toast.makeText(this, "ერთ-ერთი ველი ცარიელია. ავტორიზაციისთვის საჭიროა ყველა ველის შევსება!", Toast.LENGTH_LONG).show()
                }
                else{
                    if(captchaImageView.isVisible and captchaEditText.text.toString().isEmpty()){
                        Toast.makeText(this, "ერთ-ერთი ველი ცარიელია. ავტორიზაციისთვის საჭიროა ყველა ველის შევსება!", Toast.LENGTH_LONG).show()
                    }
                    if(captchaImageView.isVisible and captchaEditText.text.toString().isNotEmpty()){
                        sendRequest(emailEditText.text.toString(), passwordEditText.text.toString(), captchaEditText.text.toString())
                    }
                    if(captchaImageView.isGone){
                        sendRequest(emailEditText.text.toString(), passwordEditText.text.toString(), null)
                    }
                }
            }
            else{
                Toast.makeText(this, "ავტორიზაციისთვის საჭიროა ინტერნეტთან წვდომა. გთხოვთ შეამოწმოთ თქვენი კავშირი ინტერნეტთან!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendRequest(username: String, password: String, code: String?) {
        class DemoTask: AsyncTask<Void, Void, Void>() {

            lateinit var doc : Connection.Response
            lateinit var parsedDoc : Document
            override fun doInBackground(vararg params: Void): Void? {
                val task = this
                try{
                    if(code != null){
                        doc = Jsoup.connect("https://classroom.btu.edu.ge/ge/login/trylogin")
                            .data("username", username)
                            .data("password", password)
                            .data("code", code)
                            .data(mCookies)
                            // and other hidden fields which are being passed in post request.
                            .userAgent("Mozilla")
                            .method(Connection.Method.POST)
                            .execute()

                    }
                    else{
                        doc = Jsoup.connect("https://classroom.btu.edu.ge/ge/login/trylogin")
                            .data("username", username)
                            .data("password", password)
                            // and other hidden fields which are being passed in post request.
                            .userAgent("Mozilla")
                            .method(Connection.Method.POST)
                            .execute()

                    }
                }
                catch (e: IOException){
                    task.cancel(true)
                    displayError(this@LoginActivity, "ვერ მოხერხდა საიტთან დაკავშირება! სცადეთ თავიდან.")
                }

                return null
            }

            override fun onPostExecute(result: Void?) {
                parsedDoc = doc.parse()
                if(parsedDoc.location() == "https://classroom.btu.edu.ge/ge/login"){
                    if(parsedDoc.select("span[class=alert-message]").text().contains("დამცავი", false)){
                        if(!autoLogin){
                            getCaptcha()
                        }
                        else {
                            setContentView(R.layout.activity_login)
                            init()
                            getCaptcha()
                            autoLogin = false
                        }
                        Toast.makeText(this@LoginActivity, "კლასრუმი მოითხოვს დაცვის კოდის შეყვანას!", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this@LoginActivity, "მითითებული ინფორმაცია არასწორია!", Toast.LENGTH_SHORT).show()
                }
                else{
                    writeStringCache(this@LoginActivity, "cookies", doc.cookies().toString())
                    if(!autoLogin)
                    {
                        writeBooleanCache(this@LoginActivity, "firstTime", true)
                        writeStringCache(this@LoginActivity, "username", username)
                        writeStringCache(this@LoginActivity, "password", password)
                        val data = GetWebData()
                        data.getUserRating(this@LoginActivity, getStringCache(this@LoginActivity, "cookies"))
                        data.getUserCredits(this@LoginActivity, getStringCache(this@LoginActivity, "cookies"))
                        data.getUserImage(this@LoginActivity, getStringCache(this@LoginActivity, "cookies"))

                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    private fun getCaptcha(){
        class DemoTask: AsyncTask<Void, Void, Void>() {

            lateinit var doc : Connection.Response
            override fun doInBackground(vararg params: Void): Void? {
                doc = Jsoup.connect("https://classroom.btu.edu.ge/ge/login/captcha")
                    // and other hidden fields which are being passed in post request.
                    .userAgent("Mozilla")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()

                mCookies = doc.cookies()
                return null
            }

            override fun onPostExecute(result: Void?) {
                val bitmaps = BitmapFactory.decodeStream(doc.bodyAsBytes().inputStream())
                captchaImageView.visibility = View.VISIBLE
                captchaEditText.visibility = View.VISIBLE
                captchaImageView.setImageBitmap(bitmaps)

                super.onPostExecute(result)
            }
        }
        val demoTask = DemoTask()
        demoTask.execute()
    }

    private fun makeOfflineLogIn(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
