package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.pojo.Meal
import com.example.foodfinder11.pojo.MealList
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var allMealsLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getAllMealsByRandomLetter() {
        RetrofitInstance.api.getAllMeals(getRandomLetter()).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
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
    }

    fun observeRandomMealLiveData() : LiveData<Meal>{
        return randomMealLiveData
    }

    fun observeAllMealsLiveData() : LiveData<List<Meal>>{
        return allMealsLiveData
    }

    fun getRandomLetter(): String {
        val alphabet = "abcdefghijklmnoprstvwy"
        val randomIndex = (0 until alphabet.length).random()
        val letter = alphabet[randomIndex].toString()
        return letter
    }
}