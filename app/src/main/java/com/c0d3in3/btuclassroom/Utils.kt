package com.c0d3in3.btuclassroom

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*


fun writeStringCache(context: Context, name : String, value : String){
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    val editor = sharedPref.edit()
    editor.putString(name, value)
    editor.apply()
}

fun writeLongCache(context: Context, name : String, value : Long){
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    val editor = sharedPref.edit()
    editor.putLong(name, value)
    editor.apply()
}

fun writeFloatCache(context: Context, name : String, value : Float){
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    val editor = sharedPref.edit()
    editor.putFloat(name, value)
    editor.apply()
}

fun writeBooleanCache(context: Context, name : String, value : Boolean){
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    val editor = sharedPref.edit()
    editor.putBoolean(name, value)
    editor.apply()
}

fun getStringCache(context: Context, name : String) : String{
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    return sharedPref.getString(name, "").toString()
}

fun getLongCache(context: Context, name : String) : Long{
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    return sharedPref.getLong(name, 0)
}

fun getFloatCache(context: Context, name : String) : Float{
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    return sharedPref.getFloat(name, 0.0F)
}

fun getBooleanCache(context: Context, name : String) : Boolean{
    var PRIVATE_MODE = 0
    val MY_PREFS = "MY_PREFS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(MY_PREFS, PRIVATE_MODE)
    return sharedPref.getBoolean(name, false)
}

fun getDayInt(day : String) : Int{
    return when(day){
        "ორშაბათი" -> 2
        "სამშაბათი" -> 3
        "ოთხშაბათი" -> 4
        "ხუთშაბათი" -> 5
        "პარასკევი" -> 6
        "შაბათი" -> 7
        "კვირა" -> 1
        else -> -1
    }
}

fun getDayString(day : Int) : String{
    return when(day){
        2 -> "ორშაბათი"
        3 -> "სამშაბათი"
        4 -> "ოთხშაბათი"
        5 -> "ხუთშაბათი"
        6 -> "პარასკევი"
        7 -> "შაბათი"
        1 -> "კვირა"
        else -> "null"
    }
}

fun getClosestLecture(context: Context) : Int{
    var dif = -10
    var nextLectureDay = 0
    val data = Schedule(context)
    val schedules = data.getSchedule()
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    for (_day in 0 until schedules.size) {
        if (day == schedules[_day]._day) {
            if (hour < schedules[_day]._time.substring(0, 2).toInt()) {
                nextLectureDay = _day
                break
            }
        }
        else {
            if(dif < day-schedules[_day]._day && schedules[_day]._day > day) {
                dif = day - schedules[_day]._day
                nextLectureDay = _day
            }
        }
    }
    return nextLectureDay
}

fun networkAvaliable(activity: AppCompatActivity):Boolean{
    val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo=connectivityManager.activeNetworkInfo
    return  networkInfo!=null && networkInfo.isConnected
}
