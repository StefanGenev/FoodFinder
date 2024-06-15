package com.example.foodfinder11.activities

import android.content.Intent
import com.example.foodfinder11.databinding.ActivitySuccessfulOrderBinding
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.getParcelableExtraProvider

class SuccessfulOrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivitySuccessfulOrderBinding
    private lateinit var restaurant: Restaurant

    companion object {
        const val RESTAURANT = "restaurant"
    }


    override fun initializeActivity() {
        binding = ActivitySuccessfulOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(ReviewActivity.RESTAURANT)!!
    }

    override fun initializeViews() {

        binding.leaveReview.setOnClickListener {

            val intent = Intent(this@SuccessfulOrderActivity, ReviewActivity::class.java)
            intent.putExtra(RESTAURANT, restaurant)
            startActivity(intent)
        }
    }

    override fun commitData(): Boolean {

        ActivityUtils.openMainActivityByRole(this@SuccessfulOrderActivity)

        return true
    }
}