package com.example.foodfinder11.activities

import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodfinder11.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseNavigatableActivity() {

    override fun initializeActivity() {

        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}