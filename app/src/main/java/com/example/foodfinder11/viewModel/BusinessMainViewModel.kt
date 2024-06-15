package com.example.foodfinder11.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessMainViewModel : ViewModel() {

    private val ordersLiveData = MutableLiveData<List<Order>>()
    private var ordersLoaded = MutableLiveData<Boolean>(false)

    fun loadOrders() {

        if (ordersLoaded.value == true)
            return

        val restaurantId = SessionManager.fetchRestaurantDetails().restaurant.id
        val dto = IdentifierDto(restaurantId)

        RetrofitInstance.getApiService().getOrdersByRestaurantId(dto).enqueue(object :

            Callback<ResponseWrapper<List<Order>>> {

            override fun onResponse(
                call: Call<ResponseWrapper<List<Order>>>,
                response: Response<ResponseWrapper<List<Order>>>
            ) {

                val responseBody = response.body().takeIf { it != null } ?: return

                if (responseBody.status == 200) {

                    val responseData = responseBody.data.takeIf { it != null } ?: return

                    ordersLiveData.value = responseData
                    ordersLoaded.value = true

                } else {
                    //TODO: Log
                }

            }

            override fun onFailure(call: Call<ResponseWrapper<List<Order>>>, t: Throwable) {
                Log.d("AdminViewModel", t.message.toString())
            }
        })
    }

    fun getOrdersLiveData(): LiveData<List<Order>> {
        return ordersLiveData
    }

}
