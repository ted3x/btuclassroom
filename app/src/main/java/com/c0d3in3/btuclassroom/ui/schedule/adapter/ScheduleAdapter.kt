package com.c0d3in3.btuclassroom.ui.schedule.adapter

import android.content.Context
import ge.ted3x.core.base.BaseAdapter
import com.c0d3in3.btuclassroom.model.Lecture
import com.c0d3in3.btuclassroom.ui.schedule.view.LectureView

class ScheduleAdapter : ge.ted3x.core.base.BaseAdapter<Lecture>() {

    private var currentDay = 0
    override fun getViewHolder(context: Context) = BaseViewHolder(LectureView(context))
    override fun getLayout(): Int? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val lectureView = holder.itemView as LectureView
        val lecture = itemsList[position]
        if(lecture.day != currentDay){
            currentDay = lecture.day!!
            lectureView.initLecture(itemsList[position], currentDay)
        }
        else lectureView.initLecture(itemsList[position])
    }
}