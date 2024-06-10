package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class OrderStatuses {

    INITIALISED,
    ORDERED,
    DELIVERED,
}

@Parcelize
data class Order(
    var id: Long = 0,
    var restaurantId: Long = 0,
    var userId: Long = 0,
    var status: OrderStatuses = OrderStatuses.INITIALISED,
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

}