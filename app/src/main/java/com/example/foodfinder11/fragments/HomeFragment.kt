package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.viewModel.MainViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private var currentRestaurant: Restaurant = Restaurant()

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun loadRestaurants() {
        if (!mainViewModel.getRestaurantsLoaded())
            mainViewModel.loadAllRestaurants()
    }

    private fun openCurrentRestaurant(){
        //TODO Change with restaurant
        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(RESTAURANT, currentRestaurant)
        startActivity(intent)

    }

    private fun showCurrentRestaurant() {
        val currentRestaurantIndex = mainViewModel.getCurrentRestaurantIndex()

        mainViewModel.getAllRestaurantsLiveData().observe(viewLifecycleOwner, Observer { restaurants ->
            currentRestaurant = restaurants[currentRestaurantIndex]

            Glide.with(this@HomeFragment)
                .load(currentRestaurant.imageUrl)
                .into(binding.imgRandomMeal)


            binding.tvMealName.text = currentRestaurant.name
        })
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
}