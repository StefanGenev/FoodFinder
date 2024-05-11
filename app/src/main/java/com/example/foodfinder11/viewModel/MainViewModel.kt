package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel: ViewModel() {
    private val allRestaurantsLiveData = MutableLiveData<List<Restaurant>>()
    private var currentRestaurantIndex = MutableLiveData<Int>(0)
    private var restaurantsLoaded = MutableLiveData<Boolean>(false)

    fun loadAllRestaurants() {
        RetrofitInstance.getApiService().getAllRestaurants().enqueue(object :
            Callback<ResponseWrapper<List<Restaurant>>> {
            override fun onResponse(call: Call<ResponseWrapper<List<Restaurant>>>, response: Response<ResponseWrapper<List<Restaurant>>>) {
                val responseBody = response.body().takeIf {it != null} ?: return
                val responseData = responseBody.data.takeIf {it != null} ?: return

                allRestaurantsLiveData.value = responseData
                restaurantsLoaded.value = true
                currentRestaurantIndex.value = 0
            }

            override fun onFailure(call: Call<ResponseWrapper<List<Restaurant>>>, t: Throwable) {
                Log.d("MainViewModel", t.message.toString())
            }
        })
    }

    fun getAllRestaurantsLiveData() : LiveData<List<Restaurant>>{
        return allRestaurantsLiveData
    }

    fun getCurrentRestaurantIndex(): Int {
        return currentRestaurantIndex.value ?: 0
    }

    fun getRestaurantsLoaded(): Boolean {
        return restaurantsLoaded.value ?: false
    }

    fun moveToNextRestaurant(){
        val currentValue = currentRestaurantIndex.value ?: 0

        var newValue = currentValue + 1
        if ( newValue > (allRestaurantsLiveData.value?.size ?: 0) - 1 )
            newValue = 0

        currentRestaurantIndex.value = newValue
    }
}