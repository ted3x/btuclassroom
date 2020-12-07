package com.c0d3in3.btuclassroom.ui.mail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler.MAIL_URL
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.c0d3in3.btuclassroom.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider.getResourceString

class MailsViewModel : BaseViewModel() {

    val mails = MutableLiveData<List<Mail>>()

    init {
       getEmails()
    }

    private fun getEmails() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = NetworkHandler.getDocument(MAIL_URL, NetworkMethod.GET, null, true)){
                is Result.Success ->{
                    val parsedData = result.data.parse()
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
                    mails.postValue(list)
                }
                is Result.Error -> message.postValue(getResourceString(R.string.error_while_getting_mails))
            }
        }
    }
}
