package com.example.foodfinder11.model

data class User(
    var id: Long = 0,

    var name: String = "",

    var email: String = "",

    var password: String = "",

    var role: Roles = Roles.CUSTOMER
)