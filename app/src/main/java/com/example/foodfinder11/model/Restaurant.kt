package com.example.foodfinder11.model

import java.sql.Blob

enum class PriceRanges {
    CHEAP, MIDRANGE, EXPENSIVE
}
data class Restaurant(
    var id: Long = 0,

    var name: String = "",

    var priceRange: PriceRanges = PriceRanges.CHEAP,

    var address: String = "",

    var image: ByteArray
)