package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityCongratulationsBinding
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.SessionManager

class CongratulationsActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityCongratulationsBinding

    override fun initializeActivity() {
        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            binding.headerTitle.text = "Registration successful!"
            binding.subtitle.text = "Your profile is ready to use"

        } else {

            binding.headerTitle.text = "Successfully registered!"
            binding.subtitle.text = "As a business, you need to enter some data such as cover photo, food type and location, that the customers will see. You can do so now or login later to fill in the data."
        }
    }

    override fun commitData(): Boolean {

        ActivityUtils.openMainActivityByRole(this@CongratulationsActivity)

        return true
    }
}