package com.example.foodfinder11.model

data class Promotion(

    var id: Long = 0,
    var meal: Meal,
    var type: PromotionTypes = PromotionTypes.PERCENT,
    var promotionPercent: Int = 0,
    var additionalMealsCount: Int = 0,
)