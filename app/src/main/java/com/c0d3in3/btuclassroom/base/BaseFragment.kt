package com.c0d3in3.btuclassroom.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.c0d3in3.btuclassroom.utils.toast

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected var viewModel : VM? = null
    protected open var viewModelToken: Class<VM>? = null

    abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = providerViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel != null)
            onBindViewModel(viewModel!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }


    @CallSuper
    open fun onBindViewModel(viewModel: VM) {
        viewModel.message.observe(viewLifecycleOwner, Observer { message->
            requireContext().toast(message)
        })
    }

    private fun providerViewModel() =
        if (viewModelToken != null) ViewModelProvider(this)[viewModelToken!!] else null

    protected fun navigate(actionId: Int, bundle: Bundle? = null){
        findNavController().navigate(actionId, bundle)
    }
}