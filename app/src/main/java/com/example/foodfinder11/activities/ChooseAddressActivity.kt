package com.example.foodfinder11.activities

import com.example.foodfinder11.databinding.ActivityChooseAddressBinding
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.utils.getParcelableExtraProvider

class ChooseAddressActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityChooseAddressBinding

    private var registerRestaurantRequestDto: RegisterRestaurantRequestDto = RegisterRestaurantRequestDto()

    override fun initializeActivity() {
        binding = ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        registerRestaurantRequestDto = intent.getParcelableExtraProvider<RegisterRestaurantRequestDto>(ViewChosenPhotoActivity.REGISTER_RESTAURANT_DTO) ?: RegisterRestaurantRequestDto()
    }

    override fun initializeViews() {

        binding.selectLocationLayout.setOnClickListener {
            onClickSelectLocation()
        }

    }

    private fun onClickSelectLocation() {

    }
}