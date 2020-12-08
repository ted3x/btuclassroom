package com.c0d3in3.btuclassroom.ui.schedule.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.utils.getDayString
import kotlinx.android.synthetic.main.lecture_header_item.view.*
import kotlinx.android.synthetic.main.lecture_item.view.*

class LectureView(context: Context?) :
    LinearLayout(context) {

    init {
        orientation = VERTICAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
    }

    private fun addHeader(day: Int){
        val headerView = LayoutInflater.from(context).inflate(R.layout.lecture_header_item, this, false)
        headerView.lectureHeaderTextView.text = getDayString(day)
        this.addView(headerView, 0)
    }

    fun initLecture(lecture: Lecture, day: Int? = null){
        val lectureView = LayoutInflater.from(context).inflate(R.layout.lecture_item,this, false)
        lectureView.apply{
            lectureNameTextView.text = lecture.shortLectureName
            lecturerTextView.text = lecture.lecturer
            lectureTimeTextView.text = "${lecture.startTime} - ${lecture.endTime}"
            lectureRoomTextView.text = lecture.room
        }
        this.addView(lectureView)
        if(day != null) addHeader(day)
    }

}