package com.c0d3in3.btuclassroom.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.c0d3in3.btuclassroom.App
import java.util.*


fun getDayInt(day: String): Int {
    return when (day) {
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

fun getDayString(day: Int): String {
    return when (day) {
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

fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        App.instance.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(nw)
        return if (actNw == null) false
        else {
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    true
                else -> false
            }
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo
        return nwInfo.isConnected
    }
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.setVisible(){
    this.visibility = View.VISIBLE
}

fun View.setInvisible(){
    this.visibility = View.INVISIBLE
}

fun View.setGone(){
    this.visibility = View.GONE
}