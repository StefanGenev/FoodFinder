package com.example.foodfinder11.utils

import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.User
import com.google.gson.Gson


class SessionManager {

    companion object {

        /**
         * Function to save auth token
         */
        fun saveAuthToken(token: String) {
            AppPreferences.token = token
        }

        /**
         * Function to fetch auth token
         */
        fun fetchAuthToken(): String? {
            return AppPreferences.token
        }

        /**
         * Function to save refresh token
         */
        fun saveRefreshToken(token: String) {
            AppPreferences.refreshToken = token
        }

        /**
         * Function to fetch refresh token
         */
        fun fetchRefreshToken(): String? {
            return AppPreferences.refreshToken
        }

        fun saveUserData(user: User) {

            val gson = Gson()
            val json = gson.toJson(user)
            AppPreferences.user = json
        }

        fun saveFavoriteRestaurants(list: List<Restaurant>) {

            var user = fetchUserData()
            user.favoriteRestaurants = list
            saveUserData(user)
        }


        fun fetchUserData(): User {

            val gson = Gson()
            val json: String = AppPreferences.user!!

            val user = gson.fromJson(
                json,
                User::class.java
            )

            return user
        }

        fun saveRestaurantId(id: Long) {
            AppPreferences.restaurantId = id
        }

        fun fetchRestaurantId(): Long? {
            return AppPreferences.restaurantId
        }

        fun fetchOrder(): Order {

            var order = Order()
            val orderString = AppPreferences.order!!

            if (orderString.isEmpty()) {

                saveOrder(order)

            } else {

                val gson = Gson()
                order = gson.fromJson(
                    orderString,
                    Order::class.java
                )
            }

            return order
        }

        fun fetchOrderItems(): MutableList<OrderItem> {

            val order = fetchOrder()
            return order.orderItems
        }

        fun fetchOrderByItemMealId(mealId: Long): OrderItem? {

            val orderItems = SessionManager.fetchOrderItems()
            val orderItem = orderItems.filter { item -> item.meal.id == mealId }.firstOrNull()

            return orderItem
        }

        fun saveOrderItem(orderItem: OrderItem) {

            var orderItems = fetchOrderItems()
            orderItems = orderItems.filter { item -> item.meal.id != orderItem.meal.id }.toMutableList()

            orderItems.add(orderItem)

            saveOrderItems(orderItems)
        }

        fun saveOrderItems(orderItems: MutableList<OrderItem>) {

            var order = fetchOrder()
            order.orderItems = orderItems

            saveOrder(order)
        }

        fun saveOrder(order: Order) {

            val gson = Gson()
            val json = gson.toJson(order)
            AppPreferences.order = json
        }

        fun logoutOperations() {
            AppPreferences.reinitData()
        }
    }
}