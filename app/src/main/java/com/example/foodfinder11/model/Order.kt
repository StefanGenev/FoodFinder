package com.example.foodfinder11.model

import android.content.Context
import android.os.Parcelable
import com.example.foodfinder11.R
import kotlinx.parcelize.Parcelize
import java.util.Date

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
    var address: String = "",
    var orderItems: MutableList<OrderItem> = mutableListOf(),
    var orderedOn: Date = Date(),
    var deliveredOn: Date = Date(),

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

    fun getPaymentMethodName(context: Context): String {

        var name = ""

        if (paymentMethod == PaymentMethods.CASH) {

            name = context.getString(R.string.in_cash)

        } else if (paymentMethod == PaymentMethods.CARD) {

            name = context.getString(R.string.card, cardNumber.substring(0, 3))
        }

        return name
    }

}