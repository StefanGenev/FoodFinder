package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel

    private lateinit var currentRestaurant: Restaurant

    companion object {
        const val RESTAURANT_ID = "restaurant_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
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

    private fun loadRestaurants() {
        homeMvvm.getAllRestaurants()
    }

    private fun openCurrentRestaurant(){
        //TODO Change with restaurant
        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(RESTAURANT_ID, currentRestaurant.id)
        startActivity(intent)

    }

    private fun showCurrentRestaurant() {
        val currentRestaurantIndex = homeMvvm.getCurrentRestaurantIndex()

        homeMvvm.observeAllRestaurantsLiveData().observe(viewLifecycleOwner, Observer { restaurants ->
            currentRestaurant = restaurants[currentRestaurantIndex]

            Glide.with(this@HomeFragment)
                .load(currentRestaurant.image)
                .into(binding.imgRandomMeal)


            binding.tvMealName.text = currentRestaurant.name
        })
    }

    private fun changeRestaurant(){
        homeMvvm.moveToNextRestaurant()
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