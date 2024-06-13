package com.example.foodfinder11.activities

import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityRestaurantRegisteredBinding
import com.example.foodfinder11.utils.ActivityUtils

class RestaurantRegisteredActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityRestaurantRegisteredBinding

    override fun initializeActivity() {
        binding = ActivityRestaurantRegisteredBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {
        binding.headerTitle.text = getString(R.string.restaurant_registration_complete)
        binding.subtitle.text = getString(R.string.you_can_begin_to_fill_out_your_menu)
    }

    override fun commitData(): Boolean {

        ActivityUtils.openMainActivityByRole(this@RestaurantRegisteredActivity)

        return true
    }

}