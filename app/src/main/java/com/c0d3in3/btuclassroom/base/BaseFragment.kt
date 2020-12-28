package com.c0d3in3.btuclassroom.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.c0d3in3.btuclassroom.app.AppComponent
import com.c0d3in3.btuclassroom.ui.MainActivity
import com.c0d3in3.btuclassroom.utils.toast

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    open val viewModel: VM? = null

    abstract fun getLayout(): Int
    abstract fun getTitle(): String
    abstract fun isBackArrowEnabled(): Boolean
    abstract fun toolbarButtonIcon(): Drawable?
    abstract fun injectDagger()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDagger()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbar(
            getTitle(),
            isBackArrowEnabled(),
            toolbarButtonIcon()
        ) {
            onToolbarButtonClick()
        }
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar()
        showBottomNav()
        if (viewModel != null)
            onBindViewModel(viewModel!!)
    }

    @CallSuper
    open fun onBindViewModel(viewModel: VM) {
        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            requireContext().toast(message)
        })
    }

    protected fun navigate(actionId: Int, bundle: Bundle? = null) {
        findNavController().navigate(actionId, bundle)
    }

    private fun showToolbar() {
        (activity as MainActivity).showToolbar()
    }

    protected fun hideToolbar() {
        (activity as MainActivity).hideToolbar()
    }

    private fun showBottomNav() {
        (activity as MainActivity).showBottomNav()
    }

    protected fun hideBottomNav() {
        (activity as MainActivity).hideBottomNav()
    }

    open fun onToolbarButtonClick() {}
}
