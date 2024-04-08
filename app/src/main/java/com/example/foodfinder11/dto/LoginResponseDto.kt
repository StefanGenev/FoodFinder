package com.example.foodfinder11.dto

import com.example.foodfinder11.model.User

data class LoginResponseDto(
    var accessToken: String,
    var token: String,
    var user: User
)