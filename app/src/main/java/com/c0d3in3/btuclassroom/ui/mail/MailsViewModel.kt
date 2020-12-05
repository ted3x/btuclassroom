package com.c0d3in3.btuclassroom.ui.mail

import androidx.lifecycle.MutableLiveData
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler.MAIL_URL
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.model.Mail

class MailsViewModel : BaseViewModel() {

    val mails = MutableLiveData<List<Mail>>()

    init {
       // getEmails()
    }

    /*private fun getEmails() {
        val dataDoc = NetworkHandler.getDocument(MAIL_URL, NetworkMethod.GET, null, true)
        val parsedData = dataDoc?.parse()
        if (parsedData != null) {
            val main = parsedData.select("tr[class]")
            val list = mutableListOf<Mail>()
            main.forEach { ele ->
                val arg = ele.select("td")
                val mail = Mail(
                    arg[1].text(),
                    arg[2].text(),
                    arg[3].text(),
                    arg[1].select("a").attr("href").toString(),
                    false
                )
                if (ele.className().toString() == "info") mail.unread = true
                list.add(mail)
            }
        }
    }*/
}
