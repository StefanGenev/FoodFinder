package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var restaurant: Restaurant = Restaurant()
) : Parcelable
