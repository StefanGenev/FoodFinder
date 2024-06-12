package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var restaurant: Restaurant = Restaurant(),
    var hasPromotion: Boolean = false,
    var promotionType: PromotionTypes = PromotionTypes.PERCENT,
    var promotionPercent: Int = 0,
    var isHidden: Boolean = false,
) : Parcelable {


    fun getActualPrice(count: Int = 1): Double {

        var actualPrice = price * count

        if (hasPromotion) {

            if (promotionType == PromotionTypes.PERCENT) {

                val discountPart: Double = promotionPercent.toDouble() / 100
                val discount: Double = price * discountPart
                actualPrice = price - discount
                actualPrice *= count

            } else if (promotionType == PromotionTypes.TWO_FOR_ONE) {

                val fullGroups = count / 2 // Calculate the number of full groups for one free
                val remainder = count % 2 // Calculate the remainder after full groups
                actualPrice = (price * fullGroups) + (remainder * price)
            }
        }

        return actualPrice
    }

}
