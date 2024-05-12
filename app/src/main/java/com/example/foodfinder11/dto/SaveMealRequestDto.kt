package com.example.foodfinder11.dto

import com.example.foodfinder11.model.Restaurant

data class SaveMealRequestDto(

    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var restaurantId: Long = 0

)