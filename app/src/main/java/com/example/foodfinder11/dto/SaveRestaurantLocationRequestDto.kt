package com.example.foodfinder11.dto

data class SaveRestaurantLocationRequestDto(

    var restaurantId: Long = 0,
    var address: String = ""
)