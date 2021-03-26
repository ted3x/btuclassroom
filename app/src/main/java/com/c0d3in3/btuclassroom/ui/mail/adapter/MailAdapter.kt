package com.c0d3in3.btuclassroom.ui.mail.adapter

import android.content.Context
import android.graphics.Color
import com.c0d3in3.btuclassroom.R
import ge.ted3x.core.base.BaseAdapter
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_list_item.view.*


class MailAdapter(private val onMailSelect: (Mail) -> Unit) :
    ge.ted3x.core.base.BaseAdapter<Mail>() {

    override fun getLayout(): Int? = R.layout.mail_list_item
    override fun getViewHolder(context: Context): BaseViewHolder? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val mail = itemsList[position]
        holder.itemView.apply {
            mailSenderTextView.text = mail.author
            mailTitleTextView.text = mail.title
            mailDateTextView.text = mail.date

            if (mail.unread) setBackgroundColor(Color.parseColor("#c4e3f3"))

            setOnClickListener {
                mail.unread = false
                onMailSelect(mail)
            }
        }
    }
}
