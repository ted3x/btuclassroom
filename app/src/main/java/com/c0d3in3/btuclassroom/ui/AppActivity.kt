package com.c0d3in3.btuclassroom.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.utils.goAway
import com.c0d3in3.btuclassroom.utils.show
import ge.ted3x.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class AppActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun setToolbar(
        title: String,
        isHomeEnabled: Boolean,
        toolbarButtonIcon: Drawable?,
        onClick: () -> Unit
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isHomeEnabled)
        toolbarTextView.text = title
        if (toolbarButtonIcon != null) {
            toolbarButton.background = toolbarButtonIcon
            toolbarButton.show()
            toolbarButton.setOnClickListener {
                onClick.invoke()
            }
        } else toolbarButton.goAway()
    }

    override fun showToolbar() {
        toolbarLayout.show()
    }

    override fun hideToolbar() {
        toolbarLayout.goAway()
    }

    override fun showBottomNav() {
        nav_view.show()
    }

    override fun hideBottomNav() {
        nav_view.goAway()
    }
}
