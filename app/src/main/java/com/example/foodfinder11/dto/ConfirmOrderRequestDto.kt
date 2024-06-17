package com.example.foodfinder11.dto

import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.model.PaymentMethods

data class ConfirmOrderRequestDto (
    var restaurantId: Long,
    var userId: Long,
    var paymentMethod: PaymentMethods = PaymentMethods.CASH,
    var cardNumber: String = "",
    var address: String = "",
    var deliveryPrice: Double = 0.0,
    var orderItems: MutableList<OrderItem> = mutableListOf(),
)