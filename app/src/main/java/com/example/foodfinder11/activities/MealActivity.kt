package com.example.foodfinder11.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityMealBinding
import com.example.foodfinder11.fragments.HomeFragment

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealCategory: String
    private var isFavorite: Boolean = false;
    private lateinit var binding: ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.topToolbar)

        supportActionBar?.apply {
            title = "Details"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        getMealInformation()
        setInformationInViews()
        onFavoritesButtonClick()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.tvTitle.text = mealName
        binding.tvCategory.text = "Category: " + mealCategory
        binding.tvDescription.text = "From savory bites to sweet sensations, each bite is a journey through a symphony of exquisite tastes. " +
                                     "Immerse yourself in a world of culinary bliss at ${mealName} – where every dish is a celebration of freshness, quality, and pure gastronomic joy. Join us and savor the extraordinary!"
    }

    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        mealCategory = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)!!
    }

    private fun onFavoritesButtonClick() {
        binding.favoriteButton.setOnClickListener {
            if (isFavorite) {
                binding.favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_favorite
                    )
                )
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_favorite_full
                    )
                )

                Toast.makeText(this, "Saved to favorites", Toast.LENGTH_SHORT).show()

            }
            isFavorite = !isFavorite;
        }
    }
}