package com.example.foodfinder11.dto

data class AddReviewRequestDto (
    var userId: Long,
    var restaurantId: Long,
    var rating: Int,
    var feedback: String
)