package com.example.foodfinder11.dto

import com.example.foodfinder11.model.PriceRanges

data class SaveRestaurantRequestDto(

    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var imageUrl: String = "",

    var foodTypeCode: Long = 0
)