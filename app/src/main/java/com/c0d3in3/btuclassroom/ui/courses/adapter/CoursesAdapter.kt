package com.c0d3in3.btuclassroom.ui.courses.adapter


import android.content.Context
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseAdapter
import com.c0d3in3.btuclassroom.model.Course
import kotlinx.android.synthetic.main.course_item.view.*

class CoursesAdapter: BaseAdapter<Course>() {
    override fun getViewHolder(context: Context): BaseViewHolder? = null

    override fun getLayout(): Int? = R.layout.course_item

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val course = itemsList[position]
        holder.itemView.apply {
            courseNameTextView.text = course.courseName
            coursePointsTextView.text = course.coursePoint.toString()
        }
    }
}