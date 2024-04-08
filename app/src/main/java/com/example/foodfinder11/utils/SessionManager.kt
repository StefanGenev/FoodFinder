package com.example.foodfinder11.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.foodfinder11.R

class SessionManager {

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
}