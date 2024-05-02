package com.example.foodfinder11.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.foodfinder11.R
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.model.User

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
            AppPreferences.username = user.name
            AppPreferences.userEmail = user.email
            AppPreferences.userRole = user.role.toInt()
        }

        fun fetchUserData(): User {

            val userRole = AppPreferences.userRole ?: 0

            return User(
                AppPreferences.username.orEmpty(),
                AppPreferences.userEmail.orEmpty(),
                userRole.toEnum<Roles>() ?: Roles.CUSTOMER
            )
        }
    }
}