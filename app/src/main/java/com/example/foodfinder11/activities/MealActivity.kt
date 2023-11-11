package com.example.foodfinder11.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.MenuItemsAdapter
import com.example.foodfinder11.adapters.OffersAdapter
import com.example.foodfinder11.databinding.ActivityMealBinding
import com.example.foodfinder11.fragments.HomeFragment
import com.example.foodfinder11.pojo.MealList
import com.example.foodfinder11.viewModel.HomeViewModel

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealCategory: String
    private var isFavorite: Boolean = false;
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var binding: ActivityMealBinding
    private lateinit var offersAdapter: OffersAdapter
    private lateinit var menuItemsAdapter: MenuItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

        getMealInformation()
        setInformationInViews()
        onFavoritesButtonClick()

        homeMvvm.getAllMealsByRandomLetter()
        prepareOffers()
        observeOffers()

        prepareMenuItems()
        observeMenuItems()
    }

    private fun observeMenuItems() {
        homeMvvm.observeAllMealsLiveData().observe(this, Observer { meals ->
            menuItemsAdapter.differ.submitList(meals)
        })
    }

    private fun prepareMenuItems() {
        menuItemsAdapter = MenuItemsAdapter()
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = menuItemsAdapter
        }
    }

    private fun observeOffers() {
        homeMvvm.observeAllMealsLiveData().observe(this, Observer { meals ->
            offersAdapter.differ.submitList(meals)
        })
    }

    private fun prepareOffers() {
        offersAdapter = OffersAdapter()
        binding.rvOffers.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = offersAdapter
        }
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
        binding.tvDescription.text = "Immerse yourself in a world of culinary bliss at ${mealName} ! "

        if (isFavorite) {
            binding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_favorite_full
                )
            )
        }
    }

    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        mealCategory = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)!!
        mealCategory = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)!!
        isFavorite = intent.getBooleanExtra(HomeFragment.MEAL_FAVORITE, false)!!
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