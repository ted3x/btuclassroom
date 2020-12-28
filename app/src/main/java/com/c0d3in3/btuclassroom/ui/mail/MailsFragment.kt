package com.c0d3in3.btuclassroom.ui.mail

import android.graphics.drawable.Drawable
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.utils.Constants.MAIL_MODEL_KEY
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.app.AppComponent
import com.c0d3in3.btuclassroom.ui.mail.adapter.MailAdapter
import kotlinx.android.synthetic.main.mails_fragment.*
import javax.inject.Inject

class MailsFragment : BaseFragment<MailsViewModel>() {


    @Inject
    override lateinit var viewModel: MailsViewModel

    override fun getTitle() = getString(R.string.mails)
    override fun isBackArrowEnabled() = false
    override fun getLayout() = R.layout.mails_fragment
    override fun toolbarButtonIcon(): Drawable? = null

    override fun injectDagger() {
        App.appComponent.inject(this)
    }

    private lateinit var adapter : MailAdapter

    override fun onBindViewModel(viewModel: MailsViewModel) {
        super.onBindViewModel(viewModel)

        adapter = MailAdapter{
            val bundle = bundleOf(MAIL_MODEL_KEY to it)
            navigate(R.id.action_mailsFragment_to_mailDetailFragment, bundle)
        }

        mailRecyclerView.adapter = adapter

        viewModel.mails.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

    }
}
