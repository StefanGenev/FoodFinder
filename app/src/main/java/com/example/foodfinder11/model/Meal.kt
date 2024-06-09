package com.example.foodfinder11.model

import android.graphics.Paint
import android.os.Parcelable
import android.view.View
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
    var additionalMealsCount: Int = 0,
    var isHidden: Boolean = false,
) : Parcelable {


    fun getActualPrice(count: Int = 1): Double {

        var actualPrice = price

        if (hasPromotion) {

            if (promotionType == PromotionTypes.PERCENT) {

               actualPrice = price - price * (promotionPercent / 100)

            } else if (promotionType == PromotionTypes.MANY_FOR_ONE) {

                val fullGroups = count / (additionalMealsCount + 1) // Calculate the number of full groups for one free
                val remainder = count % (additionalMealsCount + 1) // Calculate the remainder after full groups
                actualPrice = (price * fullGroups) + (remainder * price)
            }
        }

        return actualPrice
    }

}
