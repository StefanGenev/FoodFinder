package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.adapters.RestaurantsAdapter
import com.example.foodfinder11.dataObjects.RestaurantsFilter
import com.example.foodfinder11.databinding.FragmentSearchBinding
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.viewModel.MainViewModel
import java.util.Locale

interface SearchRestaurantsFilterContract {

    fun onClearFilter()

    fun getFoodTypes(): List<FoodType>

    fun getFilter(): RestaurantsFilter

    fun onApplyFilter(filter: RestaurantsFilter)
}

class SearchRestaurantsFragment : Fragment(), SearchRestaurantsFilterContract {

    private lateinit var binding: FragmentSearchBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private var restaurants: List<Restaurant> = listOf()
    private var foodTypes: List<FoodType> = listOf()

    private lateinit var filterBottomSheetDialog: SearchRestaurantsFilterBottomSheet

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

        resetAdapters()

        loadRestaurants()
        showRestaurants()
        loadFoodTypes()
        observeFoodTypes()

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

        val filter = mainViewModel.getRestaurantsFilterLiveData().value ?: RestaurantsFilter()
        binding.idSearchView.setQuery(filter.stringFilter, true)
    }

    override fun onResume() {
        super.onResume()

        loadRestaurants()
        loadFoodTypes()
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
        binding.emptyStateLayout.visibility = View.VISIBLE
    }

    private fun filter(text: String) {

        binding.emptyStateLayout.visibility = View.GONE

        val filteredlist = getFilteredList(text)

        if (filteredlist.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
        }

        var filter = mainViewModel.getRestaurantsFilterLiveData().value ?: RestaurantsFilter()
        filter.stringFilter = text
        mainViewModel.setRestaurantsFilter(filter)

        resetAdapters()
        restaurantsAdapter.differ.submitList(filteredlist)
    }

    private fun checkIfStringFilterMatches(textFilter: String, restaurant: Restaurant): Boolean {

        val nameMatches = restaurant.name.lowercase(Locale.getDefault()).contains(
            textFilter.lowercase(
                Locale.getDefault()
            )
        ) || textFilter.isEmpty()

        val foodTypeMatches = restaurant.foodType.name.lowercase(Locale.getDefault()).contains(
            textFilter.lowercase(
                Locale.getDefault()
            )
        ) || textFilter.isEmpty()

        val foodTypeEnglishMatches = restaurant.foodType.nameEnglish.lowercase(Locale.getDefault()).contains(
            textFilter.lowercase(
                Locale.getDefault()
            )
        ) || textFilter.isEmpty()

        return nameMatches || foodTypeMatches || foodTypeEnglishMatches
    }

    private fun checkIfFilterMatches(restaurant: Restaurant): Boolean {

        var filterMatches = true

        if (mainViewModel.isRestaurantsFilterApplied()) {

            val filter = mainViewModel.getRestaurantsFilterLiveData().value ?: RestaurantsFilter()

            val priceRangeMatches =
                if (filter.hasSelectedPriceRange) restaurant.priceRange == filter.priceRange else true
            val foodTypeMatches =
                if (filter.foodType.isNotEmpty()) restaurant.foodType.getLocalName() == filter.foodType else true

            filterMatches = priceRangeMatches && foodTypeMatches
        }

        return filterMatches
    }

    private fun getFilteredList(filter: String): ArrayList<Restaurant> {

        val filteredlist = ArrayList<Restaurant>()

        for (restaurant in restaurants) {

            val stringMatches = checkIfStringFilterMatches(filter, restaurant)
            val filterMatches = checkIfFilterMatches(restaurant)

            if (stringMatches && filterMatches) {
                filteredlist.add(restaurant)
            }
        }

        return filteredlist
    }

    private fun loadRestaurants() {

        if (!mainViewModel.getRestaurantsLoaded())
            mainViewModel.loadAllRestaurants()
    }

    private fun loadFoodTypes() {

        if (!mainViewModel.getFoodTypesLoaded())
            mainViewModel.loadFoodTypes()
    }

    private fun observeFoodTypes() {
        mainViewModel.getFoodTypesLiveData()
            .observe(viewLifecycleOwner, Observer { foodTypes ->

                if (foodTypes.isEmpty()) {

                } else {

                    this.foodTypes = foodTypes
                }

            })
    }

    private fun initRestaurants() {

        var filter = mainViewModel.getRestaurantsFilterLiveData().value ?: RestaurantsFilter()
        filter(filter.stringFilter)
    }

    private fun onRestaurantTap(restaurant: Restaurant) {

        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
        startActivity(intent)
    }

    private fun resetAdapters() {

        restaurantsAdapter = RestaurantsAdapter()

        restaurantsAdapter.onItemClicked(object : RestaurantsAdapter.OnItemClicked {

            override fun onClickListener(restaurant: Restaurant) {
                onRestaurantTap(restaurant)
            }

        })

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

        mainViewModel.clearRestaurantsFilter()
        filter(binding.idSearchView.query.toString())
    }

    override fun getFoodTypes(): List<FoodType> {
        return foodTypes
    }

    override fun getFilter(): RestaurantsFilter {
        return mainViewModel.getRestaurantsFilterLiveData().value ?: RestaurantsFilter()
    }

    override fun onApplyFilter(filter: RestaurantsFilter) {

        mainViewModel.setRestaurantsFilter(filter)
        filter(binding.idSearchView.query.toString())
    }

}