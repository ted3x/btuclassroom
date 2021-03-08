package com.c0d3in3.btuclassroom.ui.schedule

import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import ge.ted3x.core.base.BaseFragment
import com.c0d3in3.btuclassroom.ui.schedule.adapter.ScheduleAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : ge.ted3x.core.base.BaseFragment<ScheduleViewModel>() {

    private lateinit var adapter: ScheduleAdapter

    override var viewModelToken: Class<ScheduleViewModel>? = ScheduleViewModel::class.java

    override fun getLayout() = R.layout.schedule_fragment
    override fun getTitle() = "ცხრილი"
    override fun isBackArrowEnabled() = false
    override fun toolbarButtonIcon(): Drawable? = null

    override fun onBindViewModel(viewModel: ScheduleViewModel) {
        super.onBindViewModel(viewModel)

        adapter = ScheduleAdapter()
        scheduleRecyclerView.adapter = adapter
        viewModel.schedule.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })
    }
}