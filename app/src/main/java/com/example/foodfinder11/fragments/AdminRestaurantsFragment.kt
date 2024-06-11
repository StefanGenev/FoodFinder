package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.adapters.RestaurantsAdapter
import com.example.foodfinder11.databinding.FragmentAdminRestaurantsBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class AdminRestaurantsFragment : Fragment() {

    private lateinit var binding: FragmentAdminRestaurantsBinding

    private var restaurants: ArrayList<Restaurant> = ArrayList()

    private lateinit var restaurantsAdapter: RestaurantsAdapter

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
    }

    private fun filter(text: String) {

        val filteredlist = ArrayList<Restaurant>()

        for (restaurant in restaurants) {

            val nameMatches = restaurant.name.lowercase(Locale.getDefault()).contains(text.lowercase(
                Locale.getDefault()))

            val foodTypeMatches = restaurant.foodType.name.lowercase(Locale.getDefault()).contains(text.lowercase(
                Locale.getDefault()))

            if (nameMatches || foodTypeMatches) {

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

            }

        })

        resetAdapters()
        restaurantsAdapter.differ.submitList( restaurants )
    }

    private fun resetAdapters() {

        binding.rvRestaurants.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = restaurantsAdapter
        }
    }

}