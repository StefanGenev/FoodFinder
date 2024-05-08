package com.example.foodfinder11.model

import java.sql.Blob

enum class MealCategory {

}

data class Meal(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var image: ByteArray,
    var category: MealCategory,
)
