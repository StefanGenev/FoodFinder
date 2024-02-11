package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dto.GetAllRestaurantsResponseModel
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var allRestaurantsLiveData = MutableLiveData<List<Restaurant>>()
    val currentRestaurantIndex = MutableLiveData<Int>(0)

    fun getAllRestaurants() {
        RetrofitInstance.getApiService().getAllRestaurants().enqueue(object :
            Callback<GetAllRestaurantsResponseModel> {
            override fun onResponse(call: Call<GetAllRestaurantsResponseModel>, response: Response<GetAllRestaurantsResponseModel>) {
                if (response.body() != null) {
                    allRestaurantsLiveData.value = response.body()!!.restaurants
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<GetAllRestaurantsResponseModel>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun observeAllRestaurantsLiveData() : LiveData<List<Restaurant>>{
        return allRestaurantsLiveData
    }

    fun getCurrentRestaurantIndex(): Int {
        return currentRestaurantIndex.value ?: 0
    }

    fun moveToNextRestaurant(){
        val currentValue = currentRestaurantIndex.value ?: 0

        var newValue = currentValue + 1
        if ( newValue > (allRestaurantsLiveData.value?.size ?: 0) - 1 )
            newValue = 0

        currentRestaurantIndex.value = newValue
    }
}