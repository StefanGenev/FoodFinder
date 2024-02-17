package com.example.foodfinder11.model


enum class PriceRanges {
    CHEAP, MIDRANGE, EXPENSIVE
}
data class Restaurant(
    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var image: ByteArray = ByteArray(0),

    var rating: Double = 0.0
)