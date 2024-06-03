package com.example.foodfinder11.dto

data class AddRemoveFavoriteRestaurantRequestDto (
    var userId: Long,
    var restaurantId: Long,
    var removeFromFavorites: Boolean
)