package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.dto.GetAllRestaurantsResponseModel
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var sessionManager: SessionManager

    private lateinit var currentRestaurants: List<Restaurant>
    private lateinit var currentRestaurant: Restaurant
    private var nextRestaurantIndex: Int = 0
    private var restaurantsLoaded:Boolean = false

    companion object {
        const val MEAL_ID = "com.example.foodfinder11.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodfinder11.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodfinder11.fragments.thumbMeal"
        const val MEAL_CATEGORY = "com.example.foodfinder11.fragments.categoryMeal"
        const val MEAL_FAVORITE = "com.example.foodfinder11.fragments.isFavorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        sessionManager = SessionManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRestaurants()
        observeNextRestaurant()
        onRandomMealClick()
        onDiscardClick()
        onDetailsClick()
    }

    private fun loadRestaurants() {
        if (!isAdded)
            return

        var activity = requireActivity()

        RetrofitInstance.getApiService(activity).getAllRestaurants().enqueue(object :
            Callback<GetAllRestaurantsResponseModel> {
            override fun onResponse(call: Call<GetAllRestaurantsResponseModel>, response: Response<GetAllRestaurantsResponseModel>) {
                if (response.body() != null) {
                    currentRestaurants = response.body()!!.restaurants
                    restaurantsLoaded = currentRestaurants.isNotEmpty()
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<GetAllRestaurantsResponseModel>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    private fun showCurrentRestaurant(){
        if (!restaurantsLoaded)
            return

        //TODO Change with restaurant
        val intent = Intent(activity, RestaurantActivity::class.java)
        /*
        intent.putExtra(MEAL_ID, randomMeal.idMeal)
        intent.putExtra(MEAL_NAME, randomMeal.strMeal + " Shop")
        intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
        intent.putExtra(MEAL_CATEGORY, randomMeal.strCategory)
        startActivity(intent)

         */
    }

    private fun observeNextRestaurant() {
        if (!restaurantsLoaded)
            return

        currentRestaurant = currentRestaurants[nextRestaurantIndex]
        nextRestaurantIndex++

        if (this.nextRestaurantIndex > currentRestaurants.size - 1)
            this.nextRestaurantIndex = 0

        Glide.with(this@HomeFragment)
            .load(this.currentRestaurant.image)
            .into(binding.imgRandomMeal)

        binding.tvMealName.text = this.currentRestaurant.name

        this.restaurantsLoaded = true
    }

    private fun changeRestaurant(){
        observeNextRestaurant()
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            showCurrentRestaurant()
        }
    }

    private fun onDiscardClick() {
        binding.discardButton.setOnClickListener {
            changeRestaurant()
        }
    }

    private fun onDetailsClick() {
        binding.detailsButton.setOnClickListener {
            showCurrentRestaurant()
        }
    }
}