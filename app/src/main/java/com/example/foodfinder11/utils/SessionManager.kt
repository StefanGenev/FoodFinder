package com.example.foodfinder11.utils

import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.Roles
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

        fun fetchOrderItems(): MutableList<OrderItem> {

            val orderItemsString = AppPreferences.orderItems!!

            if (orderItemsString.isEmpty()) {

                val orderItems: MutableList<OrderItem> = mutableListOf()
                saveOrderItems(orderItems)
                return orderItems
            }

            val gson = Gson()
            return gson.fromJson<MutableList<OrderItem>>(orderItemsString, MutableList::class.java)
        }

        fun saveOrderItem(orderItem: OrderItem) {

            var orderItems = fetchOrderItems()
            orderItems.add(orderItem)
            saveOrderItems(orderItems)
        }

        fun saveOrderItems(orderItems: List<OrderItem>) {

            val gson = Gson()
            val json = gson.toJson(orderItems)
            AppPreferences.orderItems = json
        }

        fun logoutOperations() {
            AppPreferences.reinitData()
        }
    }
}