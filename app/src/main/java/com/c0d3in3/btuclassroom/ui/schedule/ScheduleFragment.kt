package com.c0d3in3.btuclassroom.ui.schedule

import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.app.AppComponent
import com.c0d3in3.btuclassroom.ui.schedule.adapter.ScheduleAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*
import javax.inject.Inject

class ScheduleFragment : BaseFragment<ScheduleViewModel>() {

    @Inject
    lateinit var adapter: ScheduleAdapter

    @Inject
    override lateinit var viewModel: ScheduleViewModel

    override fun getLayout() = R.layout.schedule_fragment
    override fun getTitle() = "ცხრილი"
    override fun isBackArrowEnabled() = false
    override fun toolbarButtonIcon(): Drawable? = null

    override fun injectDagger() {
        App.appComponent.inject(this)
    }

    override fun onBindViewModel(viewModel: ScheduleViewModel) {
        super.onBindViewModel(viewModel)

        adapter = ScheduleAdapter()
        scheduleRecyclerView.adapter = adapter
        viewModel.schedule.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })
    }
}