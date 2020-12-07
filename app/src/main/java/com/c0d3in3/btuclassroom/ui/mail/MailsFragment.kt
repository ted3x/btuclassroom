package com.c0d3in3.btuclassroom.ui.mail

import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.ui.mail.adapter.MailAdapter
import kotlinx.android.synthetic.main.mails_fragment.*

class MailsFragment : BaseFragment<MailsViewModel>() {

    private lateinit var adapter : MailAdapter

    override var viewModelToken: Class<MailsViewModel>? = MailsViewModel::class.java

    override fun getTitle() = getString(R.string.mails)
    override fun isBackArrowEnabled() = true
    override fun getLayout() = R.layout.mails_fragment
    override fun toolbarButtonIcon(): Drawable? = null

    override fun onBindViewModel(viewModel: MailsViewModel) {
        super.onBindViewModel(viewModel)

        adapter = MailAdapter{

        }

        mailRecyclerView.adapter = adapter

        viewModel.mails.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

    }
}
