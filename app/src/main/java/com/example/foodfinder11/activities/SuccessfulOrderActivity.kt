package com.example.foodfinder11.activities

import android.content.Intent
import android.view.View
import com.example.foodfinder11.databinding.ActivitySuccessfulOrderBinding
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.getParcelableExtraProvider

class SuccessfulOrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivitySuccessfulOrderBinding
    private lateinit var restaurant: Restaurant
    private var reviewPermission: Boolean = false

    companion object {
        const val RESTAURANT = "restaurant"
        const val REVIEW_PERMISSION = "review_permission"
    }


    override fun initializeActivity() {
        binding = ActivitySuccessfulOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {

        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(ReviewActivity.RESTAURANT)!!

        reviewPermission = intent.getBooleanExtra(REVIEW_PERMISSION, false)
    }

    override fun initializeViews() {

        if (!reviewPermission)
            binding.leaveReview.visibility = View.GONE

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