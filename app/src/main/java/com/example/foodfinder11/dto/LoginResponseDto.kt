package com.example.foodfinder11.dto

import com.example.foodfinder11.model.User

data class LoginResponseDto(
    var authToken: String,
    var user: User
)