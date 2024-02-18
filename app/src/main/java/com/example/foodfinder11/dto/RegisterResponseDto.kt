package com.example.foodfinder11.dto

import com.example.foodfinder11.model.User

class RegisterResponseDto (
    var authToken: String,
    var user: User
)