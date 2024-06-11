package com.example.foodfinder11.activities

import android.content.Intent
import com.example.foodfinder11.databinding.ActivityRestaurantRegisteredBinding
import com.example.foodfinder11.utils.ActivityUtils

class RestaurantRegisteredActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityRestaurantRegisteredBinding

    override fun initializeActivity() {
        binding = ActivityRestaurantRegisteredBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {
        binding.headerTitle.text = "Restaurant registration complete!"
        binding.subtitle.text = "You can begin to fill out your menu"
    }

    override fun commitData(): Boolean {

        ActivityUtils.openMainActivityByRole(this@RestaurantRegisteredActivity)

        return true
    }

}