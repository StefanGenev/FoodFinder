package com.example.foodfinder11.dto

import com.example.foodfinder11.model.RestaurantStatuses

data class ChangeRestaurantStatusRequestDto (

    var restaurantId: Long = 0,
    var status: RestaurantStatuses = RestaurantStatuses.REGISTERED,
    var note: String = ""
)