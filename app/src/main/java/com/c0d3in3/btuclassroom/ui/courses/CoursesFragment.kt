package com.c0d3in3.btuclassroom.ui.courses

import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.ui.courses.adapter.CoursesAdapter
import kotlinx.android.synthetic.main.courses_fragment.*

class CoursesFragment : BaseFragment<CoursesViewModel>() {

    override var viewModelToken: Class<CoursesViewModel>? = CoursesViewModel::class.java

    override fun getLayout()= R.layout.courses_fragment
    override fun getTitle() = getString(R.string.courses)
    override fun isBackArrowEnabled() = false
    override fun toolbarButtonIcon(): Drawable? = null

    private lateinit var adapter: CoursesAdapter

    override fun onBindViewModel(viewModel: CoursesViewModel) {
        super.onBindViewModel(viewModel)

        adapter = CoursesAdapter()

        coursesRecyclerView.adapter = adapter

        viewModel.courses.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

    }

}