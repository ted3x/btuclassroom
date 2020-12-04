package com.c0d3in3.btuclassroom.ui.dashboard

import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment

class DashboardFragment : BaseFragment<DashboardViewModel>() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun getLayout() = R.layout.dashboard_fragment

    override fun onBindViewModel(viewModel: DashboardViewModel) {
        super.onBindViewModel(viewModel)

    }

}
