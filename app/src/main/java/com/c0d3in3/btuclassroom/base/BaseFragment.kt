package com.c0d3in3.btuclassroom.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<VM : ViewModel> : Fragment() {

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


    open fun onBindViewModel(viewModel: VM) {}

    private fun providerViewModel() =
        if (viewModelToken != null) ViewModelProvider(this)[viewModelToken!!] else null
}
