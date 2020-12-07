package com.c0d3in3.btuclassroom.ui.mail_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseViewModel
import com.c0d3in3.btuclassroom.data.remote.NetworkHandler
import com.c0d3in3.btuclassroom.data.remote.NetworkMethod
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.c0d3in3.btuclassroom.model.Result
import com.c0d3in3.btuclassroom.resource_provider.ResourceProvider.getResourceString

class MailDetailViewModel : BaseViewModel() {

    val mail = MutableLiveData<Mail>()
    val mailText = MutableLiveData<String>()

    fun setMail(mail: Mail){
        this.mail.value = mail
        getMailText()
    }

    private fun getMailText(){
        viewModelScope.launch(Dispatchers.IO) {

            when(val result = NetworkHandler.getDocument(mail.value!!.url, NetworkMethod.GET, null, true)){
                is Result.Success -> {
                    val parsedResult = result.data.parse()
                    val text = parsedResult.select("div[id=message_body]").text()
                    mailText.postValue(text)
                }
                is Result.Error -> message.postValue(result.message ?: getResourceString(R.string.could_not_get_email_text))
            }
        }
    }
}
