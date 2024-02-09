package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.RestaurantList
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Restaurant>()
    private var allMealsLiveData = MutableLiveData<List<Restaurant>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getAllRestaurants().enqueue(object : Callback<RestaurantList> {
            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                if (response.body() != null) {
                    val randomMeal: Restaurant = response.body()!!.restaurants[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getAllMealsByRandomLetter() {
       /*TODO RetrofitInstance.api.getAllMeals(getRandomLetter()).enqueue(object : Callback<RestaurantList> {
            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                if (response.body() != null) {
                    allMealsLiveData.value = response.body()!!.meals
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })

        */
    }

    fun observeRandomMealLiveData() : LiveData<Restaurant>{
        return randomMealLiveData
    }

    fun observeAllMealsLiveData() : LiveData<List<Restaurant>>{
        return allMealsLiveData
    }

    fun getRandomLetter(): String {
        val alphabet = "abcdefghijklmnoprstvwy"
        val randomIndex = (0 until alphabet.length).random()
        val letter = alphabet[randomIndex].toString()
        return letter
    }
}