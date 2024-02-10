package com.example.foodfinder11.retrofit

import com.example.foodfinder11.dto.GetAllRestaurantsResponseModel
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APItiteService {

    @POST("/api/login")
    fun login(@Body requestData: LoginRequestDto) : Call<LoginResponseDto>

    @POST("/api/register")
    fun register(@Body requestData: RegisterRequestDto) : Call<RegisterResponseDto>

    @GET("/api/restaurants/get-all")
    fun getAllRestaurants() : Call<GetAllRestaurantsResponseModel>
}