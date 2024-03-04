package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.adapters.ReviewsAdapter
import com.example.foodfinder11.databinding.ActivityReviewsBinding
import com.example.foodfinder11.viewModel.MainViewModel

class ReviewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var homeMvvm: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Reviews"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        reviewsAdapter = ReviewsAdapter()
        binding.rvReviews.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = reviewsAdapter
        }

        //TODO Initialize viewmodel correctly
        //homeMvvm = ViewModelProvider(this)[MainViewModel::class.java]
       // homeMvvm.getAllRestaurants()

        observeReviews()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun observeReviews() {
        homeMvvm.getAllRestaurantsLiveData().observe(this, Observer { meals ->
            //TODO reviewsAdapter.differ.submitList(meals)
        })
    }
}