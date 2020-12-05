package com.c0d3in3.btuclassroom.ui.dashboard

import android.graphics.BitmapFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.c0d3in3.btuclassroom.App
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import com.c0d3in3.btuclassroom.utils.toast
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlin.coroutines.coroutineContext

class DashboardFragment : BaseFragment<DashboardViewModel>() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override var viewModelToken: Class<DashboardViewModel>? = DashboardViewModel::class.java
    override fun getLayout() = R.layout.dashboard_fragment

    override fun onBindViewModel(viewModel: DashboardViewModel) {
        super.onBindViewModel(viewModel)

        setClickListeners()

        viewModel.yearText.observe(viewLifecycleOwner, Observer{
            yearTextView.text = it
        })

        viewModel.user.observe(viewLifecycleOwner, Observer {user->
            println("user $user")
            userNameTexView.text = user.fullName
            if(user.userImage != null) {
                //TODO EXTENSION decodeAndSetImage
                val bitmap = BitmapFactory.decodeByteArray(user.userImage, 0, user.userImage.size)
                userImageView.setImageBitmap(bitmap)
            }
        })
    }

    private fun setClickListeners(){
        mailButton.setOnClickListener {
            if(!isNetworkAvailable()) requireContext().toast(getString(R.string.you_should_have_network_connection_to_access_mails))
            else{
                if(App.cookies.isEmpty()){
                    requireContext().toast(getString(R.string.after_being_offline_you_have_to_auth_again))
                    navigate(R.id.action_dashboardFragment_to_loginFragment)
                }
                else navigate(R.id.action_dashboardFragment_to_mailsFragment)
            }
        }
    }

}
