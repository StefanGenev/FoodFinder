package com.example.foodfinder11.activities

import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodfinder11.R
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseMainActivity : BaseNavigatableActivity() {

    protected abstract val layoutResourceId: Int
    protected abstract val bottomNavigationResourceId: Int

    override fun initializeActivity() {

        setContentView(layoutResourceId)

        val bottomNavigation = findViewById<BottomNavigationView>(bottomNavigationResourceId)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)

        bottomNavigation.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
    }

    override fun setLayoutLimitFlags() {
        with(window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}