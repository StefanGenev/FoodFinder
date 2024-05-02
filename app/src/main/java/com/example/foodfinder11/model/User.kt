package com.example.foodfinder11.model

data class User(
    var name: String = "",

    var email: String = "",

    var role: Roles = Roles.CUSTOMER
)