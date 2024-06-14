package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.adapters.RestaurantsAdapter
import com.example.foodfinder11.databinding.FragmentSearchBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.viewModel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

interface SearchRestaurantsFilterContract {

    fun onClearFilter()

    fun getFoodTypes(): List<FoodType>

    fun onApplyFilter(filter: SearchRestaurantsFragment.Filter)
}

class SearchRestaurantsFragment : Fragment(), SearchRestaurantsFilterContract {

    private lateinit var binding: FragmentSearchBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private var restaurants: List<Restaurant> = listOf()
    private var foodTypes: List<FoodType> = listOf()

    private var filter: Filter = Filter()
    private var filterApplied: Boolean = false

    private lateinit var filterBottomSheetDialog: SearchRestaurantsFilterBottomSheet

    class Filter(
        var foodType: String = "",
        var priceRange: PriceRanges = PriceRanges.CHEAP,
        var hasSelectedPriceRange: Boolean = false,
    )

    private lateinit var restaurantsAdapter: RestaurantsAdapter

    companion object {
        const val RESTAURANT = "restaurant"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRestaurants()
        showRestaurants()
        loadFoodTypes()

        // below line is to call set on query text listener method.
        binding.idSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText)
                return false
            }
        })

        binding.filterButton.setOnClickListener {
            openFilterBottomSheet()
        }
    }

    private fun showRestaurants() {

        mainViewModel.getAllRestaurantsLiveData()
            .observe(viewLifecycleOwner, Observer { restaurants ->

                if (restaurants.isEmpty()) {

                    showEmptyState()

                } else {

                    this.restaurants = restaurants
                    initRestaurants()
                }

            })
    }

    private fun showEmptyState() {
        TODO("Not yet implemented")
    }

    private fun filter(text: String) {

        val filteredlist = ArrayList<Restaurant>()

        for (restaurant in restaurants) {

            val nameMatches = restaurant.name.lowercase(Locale.getDefault()).contains(
                text.lowercase(
                    Locale.getDefault()
                )
            ) || text.isEmpty()

            val foodTypeMatches = restaurant.foodType.name.lowercase(Locale.getDefault()).contains(
                text.lowercase(
                    Locale.getDefault()
                )
            ) || text.isEmpty()

            val stringMatches = nameMatches || foodTypeMatches

            var filterMatches = true

            if (filterApplied) {

                val priceRangeMatches =
                    if (filter.hasSelectedPriceRange) restaurant.priceRange == filter.priceRange else true
                val foodTypeMatches =
                    if (filter.foodType.isNotEmpty()) restaurant.foodType.name == filter.foodType else true

                filterMatches = priceRangeMatches && foodTypeMatches
            }

            if (stringMatches && filterMatches) {

                filteredlist.add(restaurant)
            }
        }

        if (filteredlist.isEmpty()) {
            //TODO: Empty state
        } else {

            resetAdapters()
            restaurantsAdapter.differ.submitList(filteredlist)
        }
    }

    private fun loadRestaurants() {

        if (!mainViewModel.getRestaurantsLoaded())
            mainViewModel.loadAllRestaurants()

    }

    private fun loadFoodTypes() {

        if (!mainViewModel.getFoodTypesLoaded())
            mainViewModel.loadFoodTypes()

        mainViewModel.getFoodTypesLiveData()
            .observe(viewLifecycleOwner, Observer { foodTypes ->

                if (foodTypes.isEmpty()) {

                } else {

                    this.foodTypes = foodTypes
                }

            })
    }

    private fun initRestaurants() {

        restaurantsAdapter = RestaurantsAdapter()

        restaurantsAdapter.onItemClicked(object : RestaurantsAdapter.OnItemClicked {

            override fun onClickListener(restaurant: Restaurant) {
                onRestaurantTap(restaurant)
            }

        })

        resetAdapters()
        restaurantsAdapter.differ.submitList(restaurants)
    }

    private fun onRestaurantTap(restaurant: Restaurant) {

        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
        startActivity(intent)
    }

    private fun resetAdapters() {

        binding.rvRestaurants.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = restaurantsAdapter
        }
    }

    private fun openFilterBottomSheet() {

        filterBottomSheetDialog = SearchRestaurantsFilterBottomSheet()
        filterBottomSheetDialog.show(parentFragmentManager, "BottomSheetDialog")
    }

    override fun onClearFilter() {
        filterApplied = false
        filter(binding.idSearchView.query.toString())
    }

    override fun getFoodTypes(): List<FoodType> {
        return foodTypes
    }

    override fun onApplyFilter(filter: Filter) {

        filterApplied = true
        this.filter = filter
        filter(binding.idSearchView.query.toString())
    }

}