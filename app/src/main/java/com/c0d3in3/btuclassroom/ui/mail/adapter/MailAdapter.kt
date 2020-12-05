package com.c0d3in3.btuclassroom.ui.mail.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.model.Mail
import kotlinx.android.synthetic.main.mail_list_item.view.*


class MailAdapter(private val onMailSelect: (Mail) -> Unit) :
    RecyclerView.Adapter<MailAdapter.ViewHolder>() {

    private var mails = listOf<Mail>()

    fun setList(mails: List<Mail>) {
        this.mails = mails
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mail = mails[position]
        holder.itemView.apply {
            mailSenderTextView.text = mail.author
            mailTitleTextView.text = mail.title
            mailDateTextView.text = mail.date
            if (mail.unread) holder.itemView.setBackgroundColor(Color.parseColor("#c4e3f3"))
            holder.itemView.setOnClickListener {
                onMailSelect(mail)
            }
        }
    }

    override fun getItemCount() = mails.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.mail_list_item, parent, false)
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
