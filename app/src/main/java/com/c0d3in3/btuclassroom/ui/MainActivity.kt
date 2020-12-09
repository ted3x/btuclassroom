package com.c0d3in3.btuclassroom.ui

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.c0d3in3.btuclassroom.R
import com.c0d3in3.btuclassroom.utils.setGone
import com.c0d3in3.btuclassroom.utils.setVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_schedule,
                R.id.navigation_courses,
                R.id.navigation_mails
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    fun setToolbar(
        title: String,
        isHomeEnabled: Boolean,
        toolbarButtonIcon: Drawable?,
        onClick: () -> Unit
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isHomeEnabled)
        toolbarTextView.text = title
        if (toolbarButtonIcon != null) {
            toolbarButton.background = toolbarButtonIcon
            toolbarButton.setVisible()
            toolbarButton.setOnClickListener {
                onClick.invoke()
            }
        } else toolbarButton.setGone()
    }

    fun showToolbar() {
        toolbarLayout.setVisible()
    }

    fun hideToolbar() {
        toolbarLayout.setGone()
    }
}
