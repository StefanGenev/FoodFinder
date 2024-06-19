package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dataObjects.AdminRestaurantsFilter
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.User
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminViewModel : ViewModel() {

    private val allRestaurantsLiveData = MutableLiveData<List<Restaurant>>()
    private var restaurantsLoaded = MutableLiveData<Boolean>(false)

    private val foodTypesLiveData = MutableLiveData<List<FoodType>>()
    private var foodTypesLoaded = MutableLiveData<Boolean>(false)

    private val restaurantsFilterLiveData = MutableLiveData<AdminRestaurantsFilter>()
    private val isRestaurantsFilterApplied = MutableLiveData<Boolean>(false)

    private val customersLiveData = MutableLiveData<List<User>>()
    private var customersLoaded = MutableLiveData<Boolean>(false)
    private var usersFilter = MutableLiveData<String>("")

    fun loadAllRestaurants() {

        RetrofitInstance.getApiService().getAllRestaurants().enqueue(object :

            Callback<ResponseWrapper<List<Restaurant>>> {

            override fun onResponse(call: Call<ResponseWrapper<List<Restaurant>>>, response: Response<ResponseWrapper<List<Restaurant>>>) {

                val responseBody = response.body().takeIf {it != null} ?: return

                if (responseBody.status == 200) {

                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    allRestaurantsLiveData.value = responseData
                    restaurantsLoaded.value = true

                } else {
                    //TODO: Log
                }

            }

            override fun onFailure(call: Call<ResponseWrapper<List<Restaurant>>>, t: Throwable) {
                Log.d("AdminViewModel", t.message.toString())
            }
        })
    }

    fun loadAllCustomers(forceReload: Boolean = false) {

        if (!forceReload && customersLoaded.value == true)
            return

        RetrofitInstance.getApiService().getCustomers()
            .enqueue(object : Callback<ResponseWrapper<List<User>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<User>>>,
                    response: Response<ResponseWrapper<List<User>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        customersLiveData.value = responseData
                        customersLoaded.value = true

                    } else {
                        //TODO: Log
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<User>>>,
                    t: Throwable
                ) {
                    Log.d("AdminViewModel", t.message.toString())
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

    fun setRestaurantsFilter(filter: AdminRestaurantsFilter) {
        restaurantsFilterLiveData.value = filter
        isRestaurantsFilterApplied.value = true
    }

    fun clearRestaurantsFilter() {
        isRestaurantsFilterApplied.value = false
    }

    fun getAllRestaurantsLiveData() : LiveData<List<Restaurant>> {
        return allRestaurantsLiveData
    }

    fun setUsersFilter(filter: String) {
        usersFilter.value = filter
    }

    fun getUsersFilter() : String {
        return usersFilter.value ?: ""
    }

    fun getAllCustomersLiveData() : LiveData<List<User>> {
        return customersLiveData
    }

    fun getFoodTypesLiveData() : LiveData<List<FoodType>> {
        return foodTypesLiveData
    }

    fun getRestaurantsFilterLiveData() : LiveData<AdminRestaurantsFilter> {
        return restaurantsFilterLiveData
    }

    fun isRestaurantsFilterApplied() : Boolean {
        return isRestaurantsFilterApplied.value ?: false
    }

    fun getRestaurantsLoaded(): Boolean {
        return restaurantsLoaded.value ?: false
    }

    fun getFoodTypesLoaded(): Boolean {
        return foodTypesLoaded.value ?: false
    }

}