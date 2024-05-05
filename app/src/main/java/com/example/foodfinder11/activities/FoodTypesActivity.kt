package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
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

    companion object {
        const val FOOD_TYPE = "food_type"
    }

    private lateinit var binding: ActivityFoodTypesBinding

    private lateinit var foodTypesAdapter: FoodTypesAdapter

    private var foodTypes: ArrayList<FoodType> = ArrayList()

    override fun initializeActivity() {

        binding = ActivityFoodTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        // Applying OnClickListener to our Adapter
        foodTypesAdapter.setOnClickListener(object :

            FoodTypesAdapter.OnClickListener {

            override fun onClick(position: Int, model: FoodType) {
                passItemsToPreviousActivity(model)
            }

        })
    }

    private fun passItemsToPreviousActivity(foodType: FoodType) {
        val data = Intent()
        data.putExtra(FOOD_TYPE, foodType);
        setResult(Activity.RESULT_OK, data)

        finish()
    }

    override fun initializeData() {

        foodTypes = intent.getParcelableArrayListExtraProvider<FoodType>(NewBusinessDataActivity.FOOD_TYPES)!!

        fillFoodTypes()
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