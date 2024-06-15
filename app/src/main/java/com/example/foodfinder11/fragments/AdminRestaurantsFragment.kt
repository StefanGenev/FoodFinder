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
import com.example.foodfinder11.dataObjects.AdminRestaurantsFilter
import com.example.foodfinder11.databinding.FragmentAdminRestaurantsBinding
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.viewModel.AdminViewModel
import java.util.Locale

interface AdminRestaurantsFilterContract {

    fun onClearFilter()

    fun getFilter(): AdminRestaurantsFilter

    fun onApplyFilter(filter: AdminRestaurantsFilter)
}

class AdminRestaurantsFragment : Fragment(), AdminRestaurantsFilterContract {

    private lateinit var binding: FragmentAdminRestaurantsBinding
    private val mainViewModel: AdminViewModel by activityViewModels()

    private var restaurants: List<Restaurant> = listOf()
    private var foodTypes: List<FoodType> = listOf()

    private lateinit var filterBottomSheetDialog: AdminRestaurantsFilterBottomSheet

    private lateinit var restaurantsAdapter: RestaurantsAdapter

    companion object {
        const val RESTAURANT = "restaurant"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminRestaurantsBinding.inflate(inflater)
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
        binding.idSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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

        val filter = mainViewModel.getRestaurantsFilterLiveData().value ?: AdminRestaurantsFilter()
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
            showEmptyState()
        }

        var filter = mainViewModel.getRestaurantsFilterLiveData().value ?: AdminRestaurantsFilter()
        filter.stringFilter = text
        mainViewModel.setRestaurantsFilter(filter)

        resetAdapters()
        restaurantsAdapter.differ.submitList(filteredlist)
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


        return nameMatches || foodTypeMatches
    }

    private fun checkIfFilterMatches(restaurant: Restaurant): Boolean {

        var filterMatches = true

        if (mainViewModel.isRestaurantsFilterApplied()) {

            val filter = mainViewModel.getRestaurantsFilterLiveData().value ?: AdminRestaurantsFilter()

            val statusMatches =
                if (filter.hasSelectedStatus) restaurant.status == filter.status else true

            filterMatches = statusMatches
        }

        return filterMatches
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

        var filter = mainViewModel.getRestaurantsFilterLiveData().value ?: AdminRestaurantsFilter()
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

    override fun onClearFilter() {
        mainViewModel.clearRestaurantsFilter()
        filter(binding.idSearchView.query.toString())
    }


    override fun getFilter(): AdminRestaurantsFilter {
        return mainViewModel.getRestaurantsFilterLiveData().value ?: AdminRestaurantsFilter()
    }

    override fun onApplyFilter(filter: AdminRestaurantsFilter) {

        mainViewModel.setRestaurantsFilter(filter)
        filter(binding.idSearchView.query.toString())
    }

    private fun openFilterBottomSheet() {

        filterBottomSheetDialog = AdminRestaurantsFilterBottomSheet()
        filterBottomSheetDialog.show(parentFragmentManager, "BottomSheetDialog")
    }

}