package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dto.GetAllRestaurantsResponseModel
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var allRestaurantsLiveData = MutableLiveData<List<Restaurant>>()

    fun getAllRestaurants() {
    }

    fun observeAllRestaurantsLiveData() : LiveData<List<Restaurant>>{
        return allRestaurantsLiveData
    }
}