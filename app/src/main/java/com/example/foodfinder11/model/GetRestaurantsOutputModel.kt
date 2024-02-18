package com.example.foodfinder11.model

import android.util.Base64

data class GetRestaurantsOutputModel(
    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var image: String = "",

    var rating: Double = 0.0
) {

    fun getRestaurant(): Restaurant {
        return Restaurant(id,
                          name,
                          priceRange,
                          address,
                          getImageByteArray(),
                          rating)
    }
    fun getImageByteArray(): ByteArray {
        return Base64.decode(image, Base64.DEFAULT)
    }
}