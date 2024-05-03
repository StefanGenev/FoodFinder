package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodType (

    var id: Long = 0,

    var name: String = "",

    ) : Parcelable
