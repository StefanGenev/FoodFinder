package com.example.foodfinder11.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import androidx.annotation.RequiresApi
import com.example.foodfinder11.utils.ImageUtils
import kotlinx.parcelize.Parcelize


enum class PriceRanges {
    CHEAP, MIDRANGE, EXPENSIVE
}

@Parcelize
data class Restaurant(
    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var imageUrl: String = "",

    var rating: Double = 0.0
) : Parcelable