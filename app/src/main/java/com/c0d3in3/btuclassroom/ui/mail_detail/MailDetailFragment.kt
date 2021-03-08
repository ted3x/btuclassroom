package com.c0d3in3.btuclassroom.ui.mail_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.Constants.MAIL_MODEL_KEY
import com.c0d3in3.btuclassroom.R
import ge.ted3x.core.base.BaseFragment
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_detail_fragment.*

class MailDetailFragment : ge.ted3x.core.base.BaseFragment<MailDetailViewModel>() {

    private lateinit var mail: Mail

    override var viewModelToken: Class<MailDetailViewModel>? = MailDetailViewModel::class.java

    override fun getTitle() = getString(R.string.mails)
    override fun isBackArrowEnabled() = true
    override fun getLayout() = R.layout.mail_detail_fragment
    override fun toolbarButtonIcon(): Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mail = arguments?.get(MAIL_MODEL_KEY) as Mail
        viewModel?.setMail(mail)
    }

    override fun onBindViewModel(viewModel: MailDetailViewModel) {
        super.onBindViewModel(viewModel)

        viewModel.mail.observe(viewLifecycleOwner, Observer {mail ->
            authorTextView.text = mail.author
            dateTextView.text = mail.date
            titleTextView.text = mail.title
        })

        viewModel.mailText.observe(viewLifecycleOwner, Observer{
            mailTextView.text = it
        })
    }
}
