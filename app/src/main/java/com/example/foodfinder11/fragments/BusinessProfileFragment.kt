package com.example.foodfinder11.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.FragmentBusinessProfileBinding
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.BusinessMainViewModel

class BusinessProfileFragment : Fragment() {

    private lateinit var binding: FragmentBusinessProfileBinding
    private val mainViewModel: BusinessMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownerId = SessionManager.fetchUserData().id
        mainViewModel.loadRestaurantByOwnerId(ownerId)

        fillRestaurantData()
    }

    private fun fillRestaurantData() {

       val restaurantData = mainViewModel.getRestaurantLiveData().value!!

        Glide.with(this@BusinessProfileFragment)
            .load(restaurantData.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurantData.foodType.name
        binding.tvTitle.text = restaurantData.name
        binding.tvRating.text = restaurantData.priceRange.name
    }
}