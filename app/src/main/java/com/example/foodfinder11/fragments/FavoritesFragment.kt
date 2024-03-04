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
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.viewModel.MainViewModel
import kotlin.math.log


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var favoritesAdapter: FavoriteRestaurantsAdapter

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
        //TODO Initialize viewmodel correctly
        //homeMvvm.getAllRestaurants()
        prepareRecyclerView()
        observeFavorites()

        favoritesAdapter.onItemClicked(object : FavoriteRestaurantsAdapter.OnFavoriteMealItemClicked {
            //TODO Chane with restaurant
            override fun onClickListener(meal: Meal) {
                val intent = Intent(activity, RestaurantActivity::class.java)
               /*  intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal + " Shop")
                intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                intent.putExtra(HomeFragment.MEAL_CATEGORY, meal.strCategory)
                intent.putExtra(HomeFragment.MEAL_FAVORITE, true)
                startActivity(intent)

                */
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

    private fun observeFavorites() {
        mainViewModel.getAllRestaurantsLiveData().observe(viewLifecycleOwner, Observer { restaurants ->
            Log.d("FavoritesFragment", restaurants[0].name)

        })
    }
}