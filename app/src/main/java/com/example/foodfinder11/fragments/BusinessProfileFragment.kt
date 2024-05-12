package com.example.foodfinder11.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.BusinessInfoActivity
import com.example.foodfinder11.activities.CongratulationsActivity
import com.example.foodfinder11.activities.MainActivity
import com.example.foodfinder11.activities.MapsActivity
import com.example.foodfinder11.activities.MealInfoActivity
import com.example.foodfinder11.adapters.MenuItemsAdapter
import com.example.foodfinder11.databinding.FragmentBusinessProfileBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessProfileFragment : Fragment() {

    companion object {
        const val RESTAURANT = "restaurant"
        const val MEAL = "meal"
    }

    private lateinit var binding: FragmentBusinessProfileBinding
    private lateinit var menuItemsAdapter: MenuItemsAdapter

    private var meals: ArrayList<Meal> = ArrayList()

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            loadMeals()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewMealButton.setOnClickListener {
            createNewMeal()
        }

        initializeMenuItemsAdapter()

        val ownerId = SessionManager.fetchUserData().id
        loadRestaurantData(ownerId)
    }

    private fun initializeMenuItemsAdapter() {

        menuItemsAdapter = MenuItemsAdapter()

        menuItemsAdapter.onItemClicked(object : MenuItemsAdapter.OnMenuItemClicked {
            override fun onClickListener(meal: Meal) {
                editMeal(meal)
            }

        })

        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = menuItemsAdapter
        }
    }

    private fun createNewMeal() {
        val intent = Intent(activity, MealInfoActivity::class.java)
        intent.putExtra(MEAL, Meal())
        activityResultLauncher.launch(intent)
    }

    private fun editMeal(meal: Meal) {

        val intent = Intent(activity, MealInfoActivity::class.java)
        intent.putExtra(MEAL, meal)
        activityResultLauncher.launch(intent)
    }

    private fun loadRestaurantData(ownerId: Long) {

        val dto = IdentifierDto(id = ownerId)

        RetrofitInstance.getApiService().getByOwnerId(dto)
            .enqueue(object : Callback<ResponseWrapper<Restaurant?>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<Restaurant?>>,
                    response: Response<ResponseWrapper<Restaurant?>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        fillRestaurantData(responseData)
                        loadMeals()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<Restaurant?>>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun loadMeals() {

        val restaurantId = SessionManager.fetchRestaurantId()!!
        val dto = IdentifierDto(id = restaurantId)

        RetrofitInstance.getApiService().getMeals(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Meal>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Meal>>>,
                    response: Response<ResponseWrapper<List<Meal>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        fillMealsData(responseData)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Meal>>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }

    private fun fillRestaurantData(restaurant: Restaurant) {

        SessionManager.saveRestaurantId(restaurant.id)

        Glide.with(this@BusinessProfileFragment)
            .load(restaurant.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurant.foodType.name
        binding.chipPrice.text = restaurant.priceRange.getName()
        binding.tvTitle.text = restaurant.name
        binding.tvRating.text = "${restaurant.rating} rating"

        binding.infoButton.setOnClickListener {

            val intent = Intent(activity, BusinessInfoActivity::class.java)
            intent.putExtra(RESTAURANT, restaurant)
            startActivity(intent)
        }
    }

    private fun fillMealsData(meals: List<Meal>) {

       if (meals.isNotEmpty()) {

           menuItemsAdapter.differ.submitList(meals)

       } else {

       }

    }
}