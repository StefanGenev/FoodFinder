package com.example.foodfinder11.model

data class Review (

    var id: Long = 0,

    var restaurant: Restaurant = Restaurant(),

    var user: User = User(),

    var rating: Int = 0,

    var feedback: String = "",

    )