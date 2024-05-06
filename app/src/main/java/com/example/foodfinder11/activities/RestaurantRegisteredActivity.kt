package com.example.foodfinder11.activities

import com.example.foodfinder11.databinding.ActivityRestaurantRegisteredBinding

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

}