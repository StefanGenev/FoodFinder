package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.adapters.FavoriteRestaurantsAdapter
import com.example.foodfinder11.databinding.FragmentFavoritesBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.viewModel.HomeViewModel


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var favoritesAdapter: FavoriteRestaurantsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

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

        homeMvvm.getAllMealsByRandomLetter()
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
        homeMvvm.observeAllMealsLiveData().observe(requireActivity(), Observer { meals ->
            Log.d("observerFavorites", meals[0].name)
            //TODO favoritesAdapter.differ.submitList(meals)
        })
    }
}