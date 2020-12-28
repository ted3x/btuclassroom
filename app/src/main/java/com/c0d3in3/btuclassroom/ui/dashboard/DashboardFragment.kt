package com.c0d3in3.btuclassroom.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.c0d3in3.btuclassroom.app.App
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.base.BaseFragment
import com.c0d3in3.btuclassroom.app.AppComponent
import com.c0d3in3.btuclassroom.utils.getDayString
import com.c0d3in3.btuclassroom.utils.getUserCreditsAsText
import com.c0d3in3.btuclassroom.utils.isNetworkAvailable
import com.c0d3in3.btuclassroom.utils.toast
import kotlinx.android.synthetic.main.dashboard_fragment.*
import javax.inject.Inject


@SuppressLint("UseCompatLoadingForDrawables")
class DashboardFragment : BaseFragment<DashboardViewModel>() {

    @Inject
    override lateinit var viewModel : DashboardViewModel

    override fun getTitle() = getString(R.string.dashboard)
    override fun isBackArrowEnabled() = false
    override fun getLayout() = R.layout.dashboard_fragment
    override fun toolbarButtonIcon() : Drawable?  = requireContext().getDrawable(R.drawable.ic_user)

    override fun injectDagger() {
        App.appComponent.inject(this)
    }

    override fun onBindViewModel(viewModel: DashboardViewModel) {
        super.onBindViewModel(viewModel)

        viewModel.yearText.observe(viewLifecycleOwner) {
            yearTextView.text = it
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            userNameTexView.text = it.fullName
            courseTextView.text = getUserCreditsAsText(requireContext(), it.userCredits)
        })

        viewModel.userImage.observe(viewLifecycleOwner) {
            userImageView.setImageBitmap(it)
        }

        viewModel.nextLecture.observe(viewLifecycleOwner) {
            lectureNameTextView.text = it.shortLectureName
            lecturerTextView.text = it.lecturer
            lectureDayTextView.text = it.day?.let { it1 -> getDayString(it1) }
            lectureTimeTextView.text = it.startTime
            lectureRoomTextView.text = it.room
        }
    }

    override fun onToolbarButtonClick() {
        super.onToolbarButtonClick()
        if (!isNetworkAvailable()) requireContext().toast(getString(R.string.you_should_have_network_connection_to_access_mails))
        else {
            if (App.cookies.isEmpty()) {
                requireContext().toast(getString(R.string.after_being_offline_you_have_to_auth_again))
                navigate(R.id.action_dashboardFragment_to_loginFragment)
            } else navigate(R.id.action_dashboardFragment_to_scheduleFragment)
        }
    }
}
