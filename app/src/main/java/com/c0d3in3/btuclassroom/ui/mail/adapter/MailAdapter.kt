package com.c0d3in3.btuclassroom.ui.mail.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.mail_list_item.view.*
import android.app.Activity
import com.c0d3in3.btuclassroom.ui.mail.MailListActivity
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.ui.mail_detail.MailDetailActivity
import com.c0d3in3.btuclassroom.writeStringCache


class MailAdapter(private val items: MutableList<MailListActivity.Mails>, private val context: Context) : RecyclerView.Adapter<MailAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mailAuthor.text = items[position].author
        holder.mailName.text = items[position].title
        holder.mailTime.text = items[position].date
        if(items[position].unread){
           holder.mailLayout.setBackgroundColor(Color.parseColor("#c4e3f3"))
        }
        holder.mailLayout.setOnClickListener{
            writeStringCache(
                context,
                "mailURL",
                items[position].mailURL
            )
            val intent = Intent(context, MailDetailActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(
                R.layout.mail_list_item,
                parent,
                false
            )
        )
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val mailAuthor: TextView = view.mailAuthorTextView
        val mailName: TextView = view.mailNameTextView
        val mailTime: TextView = view.mailTimeTextView
        val mailLayout: LinearLayout = view.mailLayout
    }


}
