package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.activities.RestaurantActivity
import com.example.foodfinder11.adapters.RestaurantsAdapter
import com.example.foodfinder11.databinding.FragmentAdminRestaurantsBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.RestaurantStatuses
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

interface AdminRestaurantsFilterContract {

    fun onClearFilter()

    fun setRestaurantStatusFilter(status: RestaurantStatuses)

    fun onApplyFilter()
}

class AdminRestaurantsFragment : Fragment(), AdminRestaurantsFilterContract {

    private lateinit var binding: FragmentAdminRestaurantsBinding

    private var restaurants: ArrayList<Restaurant> = ArrayList()

    private var restaurantStatusFilter: RestaurantStatuses = RestaurantStatuses.REGISTERED
    private var filterApplied: Boolean = false

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

        loadRestaurantsRequest()

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
    }

    private fun filter(text: String) {

        val filteredlist = ArrayList<Restaurant>()

        for (restaurant in restaurants) {

            val nameMatches = restaurant.name.lowercase(Locale.getDefault()).contains(text.lowercase(
                Locale.getDefault())) || text.isEmpty()

            val foodTypeMatches = restaurant.foodType.name.lowercase(Locale.getDefault()).contains(text.lowercase(
                Locale.getDefault())) || text.isEmpty()

            val stringMatches = nameMatches || foodTypeMatches

            val filterMatches = if (filterApplied) restaurant.status == restaurantStatusFilter else true

            if (stringMatches && filterMatches) {

                filteredlist.add(restaurant)
            }
        }

        if (filteredlist.isEmpty()) {
            //TODO: Empty state
        } else {

            resetAdapters()
            restaurantsAdapter.differ.submitList( filteredlist )
        }
    }

    private fun loadRestaurantsRequest() {

        RetrofitInstance.getApiService().getAllRestaurants()
            .enqueue(object : Callback<ResponseWrapper<List<Restaurant>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Restaurant>>>,
                    response: Response<ResponseWrapper<List<Restaurant>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        restaurants.addAll(responseData)

                        initRestaurants()

                    } else {
                        Toast.makeText(activity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Restaurant>>>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, "Problem with request", Toast.LENGTH_SHORT).show()
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
        restaurantsAdapter.differ.submitList( restaurants )
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

    override fun onClearFilter() {
        filterApplied = false
        filter(binding.idSearchView.query.toString())
    }

    override fun setRestaurantStatusFilter(status: RestaurantStatuses) {
        restaurantStatusFilter = status
    }

    override fun onApplyFilter() {
        filterApplied = true
        filter(binding.idSearchView.query.toString())
    }

    private fun openFilterBottomSheet() {

        filterBottomSheetDialog = AdminRestaurantsFilterBottomSheet()
        filterBottomSheetDialog.show(parentFragmentManager, "BottomSheetDialog")
    }

}