package com.example.foodfinder11.retrofit

import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface APItiteService {

    @POST("/api/login")
    fun login(@Body requestData: LoginRequestDto) : Call<ResponseWrapper<LoginResponseDto>>

    @POST("/api/register")
    fun register(@Body requestData: RegisterRequestDto) : Call<ResponseWrapper<RegisterResponseDto>>

    @GET("/api/restaurants/get-all")
    fun getAllRestaurants() : Call<ResponseWrapper<List<Restaurant>>>

    @Multipart
    @POST("/api/restaurants/save")
    fun saveRestaurant( @Body requestData: Restaurant ): Call<ResponseWrapper<Restaurant>>
}