package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.FoodTypesAdapter
import com.example.foodfinder11.databinding.ActivityFoodTypesBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableArrayListExtraProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTypesActivity : BaseNavigatableActivity() {

    companion object {
        const val FOOD_TYPE = "food_type"
    }

    private lateinit var binding: ActivityFoodTypesBinding

    private lateinit var foodTypesAdapter: FoodTypesAdapter

    private var foodTypes: List<FoodType>? = null

    private val startActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            prepareRecyclerView()
            loadFoodTypes()
        }
    }


    override fun initializeActivity() {

        binding = ActivityFoodTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {

        val foodTypesFromIntent = intent.getParcelableArrayListExtraProvider<FoodType>(NewBusinessDataActivity.FOOD_TYPES)
    }

    override fun initializeViews() {

        prepareRecyclerView()

        binding.addButton.setOnClickListener {
            openFoodTypeData(FoodType())
        }

        if (SessionManager.fetchUserData().role != Roles.ADMIN)
            binding.addButton.visibility = View.GONE

        if (foodTypes == null) {

            loadFoodTypes()

        } else {

            fillFoodTypes()
        }
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
                        foodTypes = responseData.toList()
                        fillFoodTypes()

                    } else {
                        Toast.makeText(this@FoodTypesActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@FoodTypesActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openFoodTypeData(foodType: FoodType) {

        val intent = Intent(this@FoodTypesActivity, FoodTypeInfoActivity::class.java)
        intent.putExtra(FOOD_TYPE, foodType)
        startActivityResultLauncher.launch(intent)
    }

    private fun passItemsToPreviousActivity(foodType: FoodType) {
        val data = Intent()
        data.putExtra(FOOD_TYPE, foodType);
        setResult(Activity.RESULT_OK, data)

        finish()
    }

    private fun prepareRecyclerView() {

        foodTypesAdapter = FoodTypesAdapter()

        foodTypesAdapter.setOnClickListener(object :

            FoodTypesAdapter.OnClickListener {

            override fun onClick(position: Int, foodType: FoodType) {

                if (SessionManager.fetchUserData().role == Roles.ADMIN) {

                    openFoodTypeData(foodType)

                } else {
                    passItemsToPreviousActivity(foodType)
                }
            }
        })

        binding.rvFoodTypes.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = foodTypesAdapter
        }
    }

    private fun fillFoodTypes() {
        foodTypesAdapter.differ.submitList(foodTypes)
    }
}