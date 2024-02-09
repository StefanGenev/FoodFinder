package com.example.foodfinder11.retrofit

import com.example.foodfinder11.model.RestaurantList
import retrofit2.Call
import retrofit2.http.GET

interface APItiteService {
    @GET("/api/restaurants/get-all")
    fun getAllRestaurants() : Call<RestaurantList>
}