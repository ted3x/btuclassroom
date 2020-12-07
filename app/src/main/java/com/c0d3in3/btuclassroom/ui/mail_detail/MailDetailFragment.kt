package com.c0d3in3.btuclassroom.ui.mail_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_detail_fragment.*

class MailDetailFragment : BaseFragment<MailDetailViewModel>() {

    private lateinit var mail: Mail

    override var viewModelToken: Class<MailDetailViewModel>? = MailDetailViewModel::class.java

    //TODO SET TO TITLE OF MAIL
    override fun getTitle() = getString(R.string.mails)
    override fun isBackArrowEnabled() = true
    override fun getLayout() = R.layout.mail_detail_fragment
    override fun toolbarButtonIcon(): Drawable? = null

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
