package com.c0d3in3.btuclassroom.ui.mail

import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.ui.mail.adapter.MailAdapter

class MailsFragment : BaseFragment<MailsViewModel>() {

    private lateinit var adapter : MailAdapter

    companion object {
        fun newInstance() = MailsFragment()
    }


    override var viewModelToken: Class<MailsViewModel>? = MailsViewModel::class.java
    override fun getLayout() = R.layout.mails_fragment

    override fun onBindViewModel(viewModel: MailsViewModel) {
        super.onBindViewModel(viewModel)

        adapter = MailAdapter{

        }

        viewModel.mails.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

    }
}
