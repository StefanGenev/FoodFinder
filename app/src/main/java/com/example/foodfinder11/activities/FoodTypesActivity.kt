package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.FoodTypesAdapter
import com.example.foodfinder11.adapters.HistoryItemsAdapter
import com.example.foodfinder11.databinding.ActivityFoodTypesBinding
import com.example.foodfinder11.databinding.ActivityHistoryBinding
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.utils.getParcelableArrayListExtraProvider

class FoodTypesActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityFoodTypesBinding

    private lateinit var foodTypesAdapter: FoodTypesAdapter

    private var foodTypes: ArrayList<FoodType> = ArrayList()

    override fun initializeActivity() {

        binding = ActivityFoodTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Food Types"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        prepareRecyclerView()
        fillFoodTypes()
    }

    override fun initializeData() {
        foodTypes = intent.getParcelableArrayListExtraProvider<FoodType>(NewBusinessDataActivity.FOOD_TYPES)!!
    }

    private fun prepareRecyclerView() {
        foodTypesAdapter = FoodTypesAdapter()

        binding.rvFoodTypes.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = foodTypesAdapter
        }
    }

    private fun fillFoodTypes() {
        foodTypesAdapter.differ.submitList(foodTypes)
    }
}