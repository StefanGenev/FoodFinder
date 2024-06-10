package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class OrderStatuses {

    INITIALISED,
    ORDERED,
    DELIVERED,
}

enum class PaymentMethods {

    CASH,
    CARD,
}

@Parcelize
data class Order(
    var id: Long = 0,
    var restaurant: Restaurant = Restaurant(),
    var user: User = User(),
    var status: OrderStatuses = OrderStatuses.INITIALISED,
    var paymentMethod: PaymentMethods = PaymentMethods.CASH,
    var cardNumber: String = "",
    var orderItems: MutableList<OrderItem> = mutableListOf(),

    ) : Parcelable {

    fun getOrderPrice(): Double {

        var price = 0.0
        for (orderItem in orderItems)
            price += orderItem.meal.getActualPrice(orderItem.count)

        return price
    }

    fun getTotalItemsCount(): Int {

        var count = 0
        for (orderItem in orderItems)
            count += orderItem.count

        return count
    }

    fun getPaymentMethodName(): String {

        var name = ""

        if (paymentMethod == PaymentMethods.CASH) {

            name = "In Cash"

        } else if (paymentMethod == PaymentMethods.CARD) {

            name = "Card: ${cardNumber.substring(0, 3)} **** **** ****"
        }

        return name
    }

}