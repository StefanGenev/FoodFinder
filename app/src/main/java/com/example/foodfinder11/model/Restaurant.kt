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

enum class RestaurantStatuses {

    REGISTERED {
        override fun getName(): String {
            return "Registered"
        }
    },

    APPROVED {
        override fun getName(): String {
            return "Approved"
        }
    },

    HIDDEN {
        override fun getName(): String {
            return "Hidden"
        }
    };

    abstract fun getName(): String
}

@Parcelize
data class Restaurant(

    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var imageUrl: String = "",

    var rating: Double = 0.0,

    var foodType: FoodType = FoodType(),

    var status: RestaurantStatuses = RestaurantStatuses.REGISTERED,

    var statusNote: String = "",

    ) : Parcelable