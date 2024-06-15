package com.example.foodfinder11.dto

import android.os.Parcelable
import com.example.foodfinder11.model.Restaurant
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantDetailsResponseDto (
    var restaurant: Restaurant = Restaurant(),
    var averageRating: Double = 0.0,
    var totalOrders: Int = 0
) : Parcelable