package com.example.foodfinder11.dto

import com.example.foodfinder11.model.User

class RegisterResponseDto (
    var accessToken: String,
    var token: String,
    var user: User
)