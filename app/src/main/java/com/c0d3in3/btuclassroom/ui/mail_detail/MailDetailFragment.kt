package com.c0d3in3.btuclassroom.ui.mail_detail

import android.os.Bundle
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_detail_fragment.*

class MailDetailFragment : BaseFragment<MailDetailViewModel>() {

    private lateinit var mail: Mail

    companion object {
        fun newInstance() = MailDetailFragment()
    }

    override var viewModelToken: Class<MailDetailViewModel>? = MailDetailViewModel::class.java
    override fun getLayout() = R.layout.mail_detail_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mail = arguments?.get("mail") as Mail
    }

    override fun onBindViewModel(viewModel: MailDetailViewModel) {
        super.onBindViewModel(viewModel)

        authorTextView.text = mail.author
        dateTextView.text = mail.date
        titleTextView.text = mail.title
        //mailTextView.text = mail.text
    }

}
