package com.example.foodfinder11.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.foodfinder11.R

class SessionManager {

    companion object {
        const val USER_TOKEN = "user_token"
    }

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
}