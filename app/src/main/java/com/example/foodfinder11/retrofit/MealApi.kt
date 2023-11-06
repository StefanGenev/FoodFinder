package com.example.foodfinder11.retrofit

import com.example.foodfinder11.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {
    @GET("random.php")
    fun getRandomMeal() : Call<MealList>
}