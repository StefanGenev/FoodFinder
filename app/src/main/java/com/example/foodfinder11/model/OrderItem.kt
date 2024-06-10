package com.example.foodfinder11.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(

    var id: Long = 0,
    var meal: Meal = Meal(),
    var count: Int = 0,

) : Parcelable