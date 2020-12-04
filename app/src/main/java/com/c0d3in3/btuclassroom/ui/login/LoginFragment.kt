package com.c0d3in3.btuclassroom.ui.login

import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.shared_preferences.SharedPreferencesHandler

class LoginFragment : BaseFragment<LoginViewModel>() {

    lateinit var mCookies : MutableMap<String, String>
    private var autoLogin = false

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun getLayout() = R.layout.login_fragment

    override fun onBindViewModel(viewModel: LoginViewModel) {
        super.onBindViewModel(viewModel)
    }
}
