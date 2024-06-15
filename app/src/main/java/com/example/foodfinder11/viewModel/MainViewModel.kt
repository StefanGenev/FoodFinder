package com.example.foodfinder11.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.R
import com.example.foodfinder11.dataObjects.RestaurantsFilter
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel: ViewModel() {

    private val allRestaurantsLiveData = MutableLiveData<List<Restaurant>>()
    private var currentRestaurantIndex = MutableLiveData<Int>(0)
    private var restaurantsLoaded = MutableLiveData<Boolean>(false)

    private val foodTypesLiveData = MutableLiveData<List<FoodType>>()
    private var foodTypesLoaded = MutableLiveData<Boolean>(false)

    private val restaurantsFilterLiveData = MutableLiveData<RestaurantsFilter>()
    private val isRestaurantsFilterApplied = MutableLiveData<Boolean>(false)

    fun loadAllRestaurants() {

        RetrofitInstance.getApiService().getAllVisibleRestaurants().enqueue(object :

            Callback<ResponseWrapper<List<Restaurant>>> {

            override fun onResponse(call: Call<ResponseWrapper<List<Restaurant>>>, response: Response<ResponseWrapper<List<Restaurant>>>) {

                val responseBody = response.body().takeIf {it != null} ?: return

                if (responseBody.status == 200) {

                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    allRestaurantsLiveData.value = responseData
                    restaurantsLoaded.value = true
                    currentRestaurantIndex.value = 0

                } else {
                    //TODO: Log
                }

            }

            override fun onFailure(call: Call<ResponseWrapper<List<Restaurant>>>, t: Throwable) {
                Log.d("MainViewModel", t.message.toString())
            }
        })
    }

    fun loadFoodTypes() {

        RetrofitInstance.getApiService().getAllFoodTypes()
            .enqueue(object : Callback<ResponseWrapper<List<FoodType>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    response: Response<ResponseWrapper<List<FoodType>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        foodTypesLiveData.value = responseData
                        foodTypesLoaded.value = true

                    } else {
                        //TODO: Log
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    t: Throwable
                ) {
                    //TODO: Log
                }
            })
    }

    fun setRestaurantsFilter(filter: RestaurantsFilter) {
        restaurantsFilterLiveData.value = filter
        isRestaurantsFilterApplied.value = true
    }

    fun clearRestaurantsFilter() {
        isRestaurantsFilterApplied.value = false
    }

    fun getAllRestaurantsLiveData() : LiveData<List<Restaurant>>{
        return allRestaurantsLiveData
    }

    fun getFoodTypesLiveData() : LiveData<List<FoodType>>{
        return foodTypesLiveData
    }

    fun getRestaurantsFilterLiveData() : LiveData<RestaurantsFilter>{
        return restaurantsFilterLiveData
    }

    fun isRestaurantsFilterApplied() : Boolean {
        return isRestaurantsFilterApplied.value ?: false
    }

    fun getCurrentRestaurantIndex(): Int {
        return currentRestaurantIndex.value ?: -1
    }

    fun getRestaurantsLoaded(): Boolean {
        return restaurantsLoaded.value ?: false
    }

    fun getFoodTypesLoaded(): Boolean {
        return foodTypesLoaded.value ?: false
    }

    fun moveToNextRestaurant(){

        val currentValue = currentRestaurantIndex.value ?: 0

        var newValue = currentValue + 1
        if ( newValue > (allRestaurantsLiveData.value?.size ?: 0) - 1 )
            newValue = 0

        currentRestaurantIndex.value = newValue
    }
}