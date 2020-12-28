package com.c0d3in3.btuclassroom.ui.mail_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.utils.Constants.MAIL_MODEL_KEY
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.app.AppComponent
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_detail_fragment.*
import javax.inject.Inject

class MailDetailFragment : BaseFragment<MailDetailViewModel>() {

    @Inject
    override lateinit var viewModel: MailDetailViewModel

    override fun getTitle() = getString(R.string.mails)
    override fun isBackArrowEnabled() = true
    override fun getLayout() = R.layout.mail_detail_fragment
    override fun toolbarButtonIcon(): Drawable? = null

    override fun injectDagger() {
        App.appComponent.inject(this)
    }

    private lateinit var mail: Mail

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
