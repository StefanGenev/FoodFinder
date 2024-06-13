package com.example.foodfinder11.activities

import android.content.Intent
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityCongratulationsBinding
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

            binding.headerTitle.text = getString(R.string.registration_successful)
            binding.subtitle.text = getString(R.string.your_profile_is_ready_to_use)

        } else {

            binding.headerTitle.text = getString(R.string.successfully_registered)
            binding.subtitle.text = getString(R.string.business_registration_successful_subtitle)
        }
    }

    override fun commitData(): Boolean {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            ActivityUtils.openMainActivityByRole(this@CongratulationsActivity)

        } else {

            val intent = Intent(this@CongratulationsActivity, NewBusinessDataActivity::class.java)
            startActivity(intent)

        }

        return true
    }
}