package com.example.foodfinder11.dto

import android.os.Parcelable
import com.example.foodfinder11.model.PriceRanges
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRestaurantRequestDto(
    var name: String = "",
    var priceRanges: PriceRanges = PriceRanges.CHEAP,
    var imageUrl: String = "",
    var address: String = "",
    var ownerId: Int = 0
) : Parcelable