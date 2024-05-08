package com.example.foodfinder11.viewModel

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Optional

class BusinessMainViewModel: ViewModel() {
    val restaurantLiveData = MutableLiveData<Restaurant>()

    fun loadRestaurantByOwnerId(id: Long) {

        val dto = IdentifierDto(id = id)

        try {
            val response = RetrofitInstance.getApiService().getByOwnerId(dto).execute()
            val responseBody = response.body().takeIf { it != null } ?: return
            val responseData = responseBody.data.takeIf { it != null } ?: return

            if (responseBody.status == 200) {
                restaurantLiveData.value = responseData
            }
        } catch (e: Exception) {
            Log.d("TRACECECECECE", e.message.toString())
        }

    }

    fun getRestaurantLiveData() : LiveData<Restaurant> {
        return restaurantLiveData
    }
}
