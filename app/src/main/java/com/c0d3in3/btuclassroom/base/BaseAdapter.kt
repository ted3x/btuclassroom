package com.c0d3in3.btuclassroom.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Item> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    abstract fun getViewHolder(context: Context): BaseViewHolder?
    abstract fun getLayout(): Int?
    protected open var itemsList = listOf<Item>()

    override fun getItemCount() = itemsList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolder(parent.context) ?: BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(getLayout()!!, parent, false)
        )

    fun setList(list: List<Item>) {
        itemsList = list
        notifyDataSetChanged()
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}