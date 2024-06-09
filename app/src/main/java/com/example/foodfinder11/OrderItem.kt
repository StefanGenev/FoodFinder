package com.example.foodfinder11


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(

    var id: Long = 0,
    var mealId: Long,
    var count: Int

) : Parcelable