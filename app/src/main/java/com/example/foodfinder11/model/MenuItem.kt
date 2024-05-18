package com.example.foodfinder11.model

data class MenuItem(

    var meal: Meal,
    var hasPromotion: Boolean = false,
    var promotionType: PromotionTypes = PromotionTypes.PERCENT,
    var promotionPercent: Int = 0,
    var additionalMealsCount: Int = 0
) {
    constructor(promotion: Promotion) : this(promotion.meal
        , true
        , promotion.type
        , promotion.promotionPercent
        , promotion.additionalMealsCount)
}