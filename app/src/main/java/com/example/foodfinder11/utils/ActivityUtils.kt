package com.example.foodfinder11.utils

import android.content.Context
import android.content.Intent
import com.example.foodfinder11.activities.AdminMainActivity
import com.example.foodfinder11.activities.BusinessMainActivity
import com.example.foodfinder11.activities.MainActivity
import com.example.foodfinder11.model.Roles

object ActivityUtils {

    fun openMainActivityByRole(packageContext: Context) {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            val intent = Intent(packageContext, MainActivity::class.java)
            packageContext.startActivity(intent)

        } else if ( userData.role == Roles.RESTAURANT ){

            val intent = Intent(packageContext, BusinessMainActivity::class.java)
            packageContext.startActivity(intent)

        } else if ( userData.role == Roles.ADMIN ){

            val intent = Intent(packageContext, AdminMainActivity::class.java)
            packageContext.startActivity(intent)

        }
    }
}