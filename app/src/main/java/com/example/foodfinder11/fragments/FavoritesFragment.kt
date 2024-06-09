package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.adapters.FavoriteRestaurantsAdapter
import com.example.foodfinder11.databinding.FragmentFavoritesBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var favoritesAdapter: FavoriteRestaurantsAdapter

    private var favoriteRestaurants: List<Restaurant> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        loadFavorites()

        favoritesAdapter.onItemClicked(object : FavoriteRestaurantsAdapter.OnFavoriteRestaurantItemClicked {

            override fun onClickListener(restaurant: Restaurant) {
               onRestaurantTapped(restaurant)
            }

        })
    }

    private fun prepareRecyclerView() {

        favoritesAdapter = FavoriteRestaurantsAdapter()

        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun loadFavorites() {

        val userData = SessionManager.fetchUserData()
        favoriteRestaurants = userData.favoriteRestaurants

        favoritesAdapter.differ.submitList( favoriteRestaurants  )
    }

    private fun onRestaurantTapped(restaurant: Restaurant) {

        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(HomeFragment.RESTAURANT, restaurant)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        loadFavorites()
    }
}