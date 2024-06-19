package com.example.foodfinder11.dto

data class AddRemoveFavoriteRestaurantRequestDto (
    var userId: Long = 0,
    var restaurantId: Long = 0,
    var removeFromFavorites: Boolean = false
)