package com.example.foodfinder11.retrofit

import android.content.Context
import com.example.foodfinder11.retrofit.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level

object RetrofitInstance {
    private const val API_BASE_URL = "http://10.0.2.2:8080/"

    private lateinit var apiService: APItiteService

    fun getApiService(context: Context): APItiteService {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(APItiteService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        val levelType: Level

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}