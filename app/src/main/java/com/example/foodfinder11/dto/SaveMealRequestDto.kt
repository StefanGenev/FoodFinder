package com.example.foodfinder11.dto

import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.PromotionTypes
import com.example.foodfinder11.utils.SessionManager

data class SaveMealRequestDto(

    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var restaurantId: Long = 0,
    var hasPromotion: Boolean = false,
    var promotionType: PromotionTypes = PromotionTypes.PERCENT,
    var additionalMealsCount: Int = 0,
    var promotionPercent: Int = 0,
    var isHidden: Boolean = false,

    ) {
    constructor( meal: Meal ) : this() {
        id = meal.id
        name = meal.name
        description = meal.description
        price = meal.price
        imageUrl = meal.imageUrl
        restaurantId = SessionManager.fetchRestaurantId()!!
        hasPromotion = meal.hasPromotion
        promotionType = meal.promotionType
        promotionPercent = meal.promotionPercent
        additionalMealsCount = meal.additionalMealsCount
        isHidden = meal.isHidden
    }
}