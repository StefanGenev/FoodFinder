package com.example.foodfinder11.dto

import com.example.foodfinder11.model.PriceRanges

data class RegisterRestaurantRequestDto(

    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var imageUrl: String = "",

    var rating: Double = 0.0,

    var ownerId: Long = 0

)