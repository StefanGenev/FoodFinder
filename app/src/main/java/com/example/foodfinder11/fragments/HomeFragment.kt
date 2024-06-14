package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.dto.AddRemoveFavoriteRestaurantRequestDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private var currentRestaurant: Restaurant = Restaurant()
    private var empty: Boolean = false

    companion object {
        const val RESTAURANT = "restaurant"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        showCurrentRestaurant()
        onRandomMealClick()
        onDiscardClick()
        onDetailsClick()
    }

    override fun onResume() {
        super.onResume()

        loadRestaurants()
        showCurrentRestaurant()
    }

    private fun loadRestaurants() {
        if (!mainViewModel.getRestaurantsLoaded())
            mainViewModel.loadAllRestaurants()
    }

    private fun openCurrentRestaurant() {

        if (empty) {
            return
        }

        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(RESTAURANT, currentRestaurant)
        startActivity(intent)

    }

    private fun showCurrentRestaurant() {

        val currentRestaurantIndex = mainViewModel.getCurrentRestaurantIndex()

        mainViewModel.getAllRestaurantsLiveData().observe(viewLifecycleOwner, Observer { restaurants ->

            if (restaurants.isEmpty()) {

                empty = true
                showEmptyState()

            } else {

                currentRestaurant = restaurants[currentRestaurantIndex]

                Glide.with(this@HomeFragment)
                    .load(currentRestaurant.imageUrl)
                    .into(binding.imgRandomMeal)

                binding.tvMealName.text = currentRestaurant.name

                initFavoriteButton()

                binding.favoriteButton.setOnClickListener { addRemoveToFavoritesRequest() }
            }

        })
    }

    private fun showEmptyState() {

        binding.bottomLayout.visibility = View.GONE

        binding.tvMealName.text = "No restaurants found"

        binding.imgRandomMeal.setImageDrawable(
            activity?.let {
            ContextCompat.getDrawable(
                it.applicationContext,
                R.drawable.empty_state
            )
        })

        binding.imgRandomMeal.scaleType = ImageView.ScaleType.CENTER_INSIDE
        binding.imgRandomMeal.setBackgroundColor(ContextCompat.getColor(activity?.applicationContext!!, R.color.white))
    }

    private fun changeRestaurant(){
        mainViewModel.moveToNextRestaurant()
        showCurrentRestaurant()
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            openCurrentRestaurant()
        }
    }

    private fun onDiscardClick() {
        binding.discardButton.setOnClickListener {
            changeRestaurant()
        }
    }

    private fun onDetailsClick() {
        binding.detailsButton.setOnClickListener {
            openCurrentRestaurant()
        }
    }

    private fun initFavoriteButton() {

        val userData = SessionManager.fetchUserData()
        val restaurantIsInFavorites = userData.favoriteRestaurants.any { item -> item.id == currentRestaurant.id }

        val drawable = if (restaurantIsInFavorites) R.drawable.like else R.drawable.like_empty

        binding.favoriteButton.setImageDrawable(
            activity?.let {
                ContextCompat.getDrawable(
                    it.applicationContext,
                    drawable
                )
            }
        )
    }

    private fun addRemoveToFavoritesRequest() {

        val userData = SessionManager.fetchUserData()
        val removeFromFavorites = userData.favoriteRestaurants.any { item -> item.id == currentRestaurant.id }

        val dto = AddRemoveFavoriteRestaurantRequestDto(userId = userData.id,
            restaurantId = currentRestaurant.id,
            removeFromFavorites = removeFromFavorites)

        RetrofitInstance.getApiService().addRemoveFavoriteRestaurant(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Restaurant>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Restaurant>>>,
                    response: Response<ResponseWrapper<List<Restaurant>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return
                    val responseData = responseBody.data.takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val toastText = if (removeFromFavorites) getString(R.string.removed_from_favorites) else getString(R.string.added_to_favorites)
                        Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()

                        SessionManager.saveFavoriteRestaurants(responseData)

                        initFavoriteButton()

                    } else {
                        Toast.makeText(activity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Restaurant>>>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })

    }

}