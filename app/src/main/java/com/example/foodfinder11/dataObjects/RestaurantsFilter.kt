package com.example.foodfinder11.dataObjects

import com.example.foodfinder11.model.PriceRanges

class RestaurantsFilter (

    var stringFilter: String = "",
    var foodType: String = "",
    var priceRange: PriceRanges = PriceRanges.CHEAP,
    var hasSelectedPriceRange: Boolean = false,
)