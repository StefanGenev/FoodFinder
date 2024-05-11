package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


enum class PriceRanges {
    CHEAP {
        override fun getName(): String {
           return "Cheap"
        }
    },

    MIDRANGE {
        override fun getName(): String {
            return "Midrange"
        }
    },

    EXPENSIVE {
        override fun getName(): String {
            return "Expensive"
        }
    };

    abstract fun getName(): String
}

@Parcelize
data class Restaurant(

    var id: Long = 0,

    var name: String = "",

    var owner: User = User(),

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var imageUrl: String = "",

    var rating: Double = 0.0,

    var foodType: FoodType = FoodType()

) : Parcelable