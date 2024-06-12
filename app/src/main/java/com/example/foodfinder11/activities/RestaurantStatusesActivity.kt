package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.adapters.RestaurantStatusesAdapter
import com.example.foodfinder11.databinding.ActivityRestaurantStatusesBinding
import com.example.foodfinder11.model.RestaurantStatuses
import com.example.foodfinder11.utils.toInt

class RestaurantStatusesActivity : BaseNavigatableActivity() {

    companion object {
        const val STATUS = "status"
    }

    private lateinit var binding: ActivityRestaurantStatusesBinding

    private lateinit var restaurantStatusesAdapter: RestaurantStatusesAdapter

    override fun initializeActivity() {

        binding = ActivityRestaurantStatusesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        // Applying OnClickListener to our Adapter
        restaurantStatusesAdapter.setOnClickListener(object :

            RestaurantStatusesAdapter.OnClickListener {

            override fun onClick(item: RestaurantStatuses) {
                passItemsToPreviousActivity(item)
            }

        })
    }

    private fun passItemsToPreviousActivity(status: RestaurantStatuses) {

        val data = Intent()
        data.putExtra(STATUS, status.toInt());
        setResult(Activity.RESULT_OK, data)

        finish()
    }

    override fun initializeData() {
        fillStatuses()
    }

    private fun prepareRecyclerView() {

        restaurantStatusesAdapter = RestaurantStatusesAdapter()

        binding.rvStatuses.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = restaurantStatusesAdapter
        }
    }

    private fun fillStatuses() {
        restaurantStatusesAdapter.differ.submitList(RestaurantStatuses.values().toMutableList())
    }
}