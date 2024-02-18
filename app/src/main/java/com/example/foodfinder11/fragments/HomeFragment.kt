package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel

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
        /*
        intent.putExtra(MEAL_ID, randomMeal.idMeal)
        intent.putExtra(MEAL_NAME, randomMeal.strMeal + " Shop")
        intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
        intent.putExtra(MEAL_CATEGORY, randomMeal.strCategory)
        startActivity(intent)

         */
    }

    private fun showCurrentRestaurant() {
        val currentRestaurantIndex = homeMvvm.getCurrentRestaurantIndex()

        homeMvvm.observeAllRestaurantsLiveData().observe(viewLifecycleOwner, Observer { restaurants ->
            var currentRestaurant = restaurants[currentRestaurantIndex]

            val imageByteArray: ByteArray = Base64.decode(currentRestaurant.image, Base64.DEFAULT)

            Glide.with(this@HomeFragment)
                .load(imageByteArray)
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