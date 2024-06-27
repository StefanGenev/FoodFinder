package com.example.foodfinder11.retrofit

import com.example.foodfinder11.retrofit.interceptors.AuthInterceptor
import com.example.foodfinder11.utils.AppPreferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    const val API_BASE_URL = "http://192.168.0.103:8080/"

    private lateinit var apiService: APItiteService

    fun getApiService(): APItiteService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(AppPreferences.apititeUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okhttpClient())
                .build()

            apiService = retrofit.create(APItiteService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with interceptor
     */
    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}