package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.databinding.ActivityNewBusinessDataBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableArrayListExtraProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewBusinessDataActivity : BaseNavigatableActivity() {

    companion object {
        const val FOOD_TYPES = "food_types"
    }

    private lateinit var binding: ActivityNewBusinessDataBinding
    private var foodTypes: ArrayList<FoodType> = ArrayList()


    override fun initializeActivity() {
        binding = ActivityNewBusinessDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        binding.foodTypesLayout.setOnClickListener {
            onClickFoodTypes()
        }

    }

    override fun loadData(): Boolean {

        loadFoodTypesRequest()

        return true
    }

    fun loadFoodTypesRequest() {

        RetrofitInstance.getApiService().getAllFoodTypes()
            .enqueue(object : Callback<ResponseWrapper<List<FoodType>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    response: Response<ResponseWrapper<List<FoodType>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        foodTypes.addAll(responseData)

                    } else {
                        Toast.makeText(this@NewBusinessDataActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@NewBusinessDataActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun onClickFoodTypes() {

        val intent = Intent(this@NewBusinessDataActivity, FoodTypesActivity::class.java)
        intent.putExtra(FOOD_TYPES, foodTypes)
        startActivity(intent)
    }
}