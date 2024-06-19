package com.example.foodfinder11.dto

data class AddReviewRequestDto (
    var userId: Long = 0,
    var restaurantId: Long = 0,
    var rating: Int = 0,
    var feedback: String = ""
)