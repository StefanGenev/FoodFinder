package com.example.foodfinder11.retrofit

import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST


interface APItiteService {

    @POST("/api/login")
    fun login(@Body requestData: LoginRequestDto) : Call<ResponseWrapper<LoginResponseDto>>

    @POST("/api/checkIfEmailExists")
    fun checkIfEmailExists(@Body requestData: CheckIfEmailExistsRequestDto) : Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>

    @POST("/api/register")
    fun register(@Body requestData: RegisterRequestDto) : Call<ResponseWrapper<RegisterResponseDto>>

    @GET("/api/restaurants/get-all")
    fun getAllRestaurants() : Call<ResponseWrapper<List<Restaurant>>>

    @Multipart
    @POST("/api/restaurants/save")
    fun saveRestaurant( @Body requestData: Restaurant ): Call<ResponseWrapper<Restaurant>>

    @GET("/api/food_types/get-all")
    fun getAllFoodTypes() : Call<ResponseWrapper<List<FoodType>>>
}