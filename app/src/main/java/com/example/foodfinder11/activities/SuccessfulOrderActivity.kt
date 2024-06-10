package com.example.foodfinder11.activities

import android.content.Intent
import com.example.foodfinder11.databinding.ActivitySuccessfulOrderBinding
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.SessionManager

class SuccessfulOrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivitySuccessfulOrderBinding

    override fun initializeActivity() {
        binding = ActivitySuccessfulOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        val userData = SessionManager.fetchUserData()

        binding.headerTitle.text = "Order successful!"
        binding.subtitle.text = "You will receive an email with more details regarding your order."
    }

    override fun commitData(): Boolean {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            val intent = Intent(this@SuccessfulOrderActivity, MainActivity::class.java)
            startActivity(intent)
        }

        return true
    }
}