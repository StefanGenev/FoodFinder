package com.example.foodfinder11.dataObjects

import com.example.foodfinder11.model.RestaurantStatuses

class AdminRestaurantsFilter (

    var stringFilter: String = "",
    var status: RestaurantStatuses = RestaurantStatuses.REGISTERED,
    var hasSelectedStatus: Boolean = false,
)