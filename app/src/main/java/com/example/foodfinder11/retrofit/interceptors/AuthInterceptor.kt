package com.example.foodfinder11.retrofit.interceptors

import android.content.Context
import android.content.SharedPreferences
import com.example.foodfinder11.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        SessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}