package com.example.foodfinder11.model

import android.content.Context
import android.os.Parcelable
import com.example.foodfinder11.R
import kotlinx.parcelize.Parcelize


enum class PriceRanges {
    CHEAP {
        override fun getName(context: Context): String {
           return context.getString(R.string.cheap)
        }
    },

    MIDRANGE {
        override fun getName(context: Context): String {
            return context.getString(R.string.medium)
        }
    },

    EXPENSIVE {
        override fun getName(context: Context): String {
            return context.getString(R.string.expensive)
        }
    };

    abstract fun getName(context: Context): String
}

enum class RestaurantStatuses {

    REGISTERED {
        override fun getName(context: Context): String {
            return context.getString(R.string.registered)
        }
    },

    APPROVED {
        override fun getName(context: Context): String {
            return context.getString(R.string.approved)
        }
    },

    HIDDEN {
        override fun getName(context: Context): String {
            return context.getString(R.string.hidden)
        }
    };

    abstract fun getName(context: Context): String
}

@Parcelize
data class Restaurant(

    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var phoneNumber: String = "",

    var imageUrl: String = "",

    var foodType: FoodType = FoodType(),

    var status: RestaurantStatuses = RestaurantStatuses.REGISTERED,

    var statusNote: String = "",

    ) : Parcelable