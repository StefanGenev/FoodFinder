package com.example.foodfinder11.dto

import com.example.foodfinder11.model.Roles

data class RegisterRequestDto(
    var email: String,
    var name: String,
    var password: String,
    var role: Roles
)