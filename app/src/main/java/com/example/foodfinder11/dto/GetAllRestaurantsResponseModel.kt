package com.example.foodfinder11.dto

import com.example.foodfinder11.model.Restaurant

data class GetAllRestaurantsResponseModel (
    var statusCode: Int,
    var message: String,
    var restaurants: List<Restaurant>
)