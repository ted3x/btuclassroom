package com.c0d3in3.btuclassroom.ui.login

import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.Observer
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.utils.isVisible
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment<LoginViewModel>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override var viewModelToken: Class<LoginViewModel>? = LoginViewModel::class.java
    override fun getLayout() = R.layout.login_fragment

    override fun onBindViewModel(viewModel: LoginViewModel) {
        super.onBindViewModel(viewModel)

        viewModel.captcha.observe(viewLifecycleOwner, Observer { byteArray->
            val bitmap = BitmapFactory.decodeStream(byteArray.inputStream())
            captchaImageView.setImageBitmap(bitmap)
        })
        viewModel.auth.observe(viewLifecycleOwner, Observer {
            navigate(R.id.action_loginFragment_to_dashboardFragment)
        })

        viewModel.loadingMessage.observe(viewLifecycleOwner, Observer {
            loginGroup.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
            loadingTextView.text = it
        })
        setClickListeners()
    }

    private fun setClickListeners(){
        loginButton.setOnClickListener {
            val username = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val captcha = if(captchaEditText.isVisible()) captchaEditText.text.toString() else null
            viewModel!!.logIn(username, password, captcha)
        }
    }
}