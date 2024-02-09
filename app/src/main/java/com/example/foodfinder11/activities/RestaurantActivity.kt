package com.example.foodfinder11.activities

import android.app.Activity
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
import com.example.foodfinder11.fragments.HomeFragment
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.databinding.ActivityRestaurantBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.viewModel.HomeViewModel
import kotlin.random.Random

class RestaurantActivity : AppCompatActivity() {
    private lateinit var restaurantId: String
    private lateinit var restaurantName: String
    private lateinit var restaurantThumb: String
    private lateinit var restaurantCategory: String
    private var isFavorite: Boolean = false;
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var binding: ActivityRestaurantBinding
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

        binding = ActivityRestaurantBinding.inflate(layoutInflater)
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
            val intent = Intent(this@RestaurantActivity, OrderActivity::class.java)
            intent.putExtra(ORDERED_ITEMS_ARRAY, orderedItemsArray)
            startActivity(intent)
        }

    }

    private fun observeMenuItems() {
        homeMvvm.observeAllMealsLiveData().observe(this, Observer { meals ->
            //TODO menuItemsAdapter.differ.submitList(meals)
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
            if (item.idMeal == meal.id) {
                item.intMealCount++
                hasItem = true
                break
            }
        }

        if (!hasItem) {
            var orderItem = OrderItem(meal.id, 1, meal.name, meal.image)
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
            //TODO offersAdapter.differ.submitList(meals)
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
            .load(restaurantThumb)
            .into(binding.imgMealDetail)

        binding.tvTitle.text = restaurantName
        binding.tvCategory.text = "Category: " + restaurantCategory

        if (isFavorite) {
            binding.favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_favorite_full
                )
            )
        }

        binding.ratingbar.rating = Random.nextInt(1, 5).toFloat()
    }

    private fun getMealInformation() {
        val intent = intent
        restaurantId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        restaurantName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        restaurantThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        restaurantCategory = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)!!
        restaurantCategory = intent.getStringExtra(HomeFragment.MEAL_CATEGORY)!!
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK)
            return

        val intent = intent
        if (data != null) {
            orderedItemsArray.clear()
            orderedItemsArray = data.getParcelableArrayListExtra(RestaurantActivity.ORDERED_ITEMS_ARRAY)!!
        }

        if (orderedItemsArray.isEmpty())
        {
            binding.orderButton.visibility = View.GONE

        }
        else
        {
            var text = "Order $selectedItems item"

            if (orderedItemsArray.size > 1)
                text += "s"

            text += "(${String.format("%.2f", totalPrice)} lv.)"

            binding.orderButton.text = text
        }

    }
}