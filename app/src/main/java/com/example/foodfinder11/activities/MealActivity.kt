package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.foodfinder11.pojo.Meal
import com.example.foodfinder11.OrderItem
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
    private var selectedItems: Int = 0;
    private var totalPrice: Float = 0.0F;
    private lateinit var orderedItemsArray: ArrayList<OrderItem>

    companion object {
        const val ORDERED_ITEMS_ARRAY = "ordered_items"
    }

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

        orderedItemsArray = ArrayList()

        binding.orderButton.setOnClickListener {
            val intent = Intent(this@MealActivity, OrderActivity::class.java)
            intent.putExtra(ORDERED_ITEMS_ARRAY, orderedItemsArray)
            startActivity(intent)
        }

    }

    private fun observeMenuItems() {
        homeMvvm.observeAllMealsLiveData().observe(this, Observer { meals ->
            menuItemsAdapter.differ.submitList(meals)
        })
    }

    private fun prepareMenuItems() {
        menuItemsAdapter = MenuItemsAdapter()

        menuItemsAdapter.onItemClicked(object : MenuItemsAdapter.OnMenuItemClicked {
            override fun onClickListener(meal: Meal) {
                addMealToOrderedItems(meal)
            }

        })

        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = menuItemsAdapter
        }
    }

    private fun addMealToOrderedItems(meal: Meal) {
        var hasItem = false
        for (item in orderedItemsArray) {
            if (item.idMeal == meal.idMeal) {
                item.intMealCount++
                hasItem = true
                break
            }
        }

        if (!hasItem) {
            var orderItem = OrderItem(meal.idMeal, 1, meal.strMeal, meal.strMealThumb)
            orderedItemsArray.add(orderItem)
        }

        selectedItems++;
        totalPrice += 10

        binding.orderButton.visibility = View.VISIBLE
        var text = "Order $selectedItems item"

        if (selectedItems > 1)
            text += "s"

        text += "(${String.format("%.2f", totalPrice)} lv.)"

        binding.orderButton.text = text

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
            } else {
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