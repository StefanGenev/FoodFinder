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
    var additionalMealsCount: Int = 0,
    var isHidden: Boolean = false,
) : Parcelable
