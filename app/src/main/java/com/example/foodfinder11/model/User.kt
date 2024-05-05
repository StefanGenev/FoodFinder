package com.example.foodfinder11.model

data class User(
    var id: Int = 0,

    var name: String = "",

    var email: String = "",

    var role: Roles = Roles.CUSTOMER
)