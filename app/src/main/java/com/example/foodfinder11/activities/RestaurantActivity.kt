package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.MenuItemsAdapter
import com.example.foodfinder11.databinding.ActivityRestaurantBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.fragments.BusinessProfileFragment
import com.example.foodfinder11.fragments.HomeFragment
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestaurantActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityRestaurantBinding
    private lateinit var promotionsAdapter: MenuItemsAdapter
    private lateinit var menuAdapter: MenuItemsAdapter

    private var restaurant: Restaurant = Restaurant()

    override fun initializeActivity() {

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
    }

    override fun initializeData() {

        val intent = intent
        restaurant = intent.getParcelableExtraProvider<Restaurant>(HomeFragment.RESTAURANT)!!
    }

    override fun initializeViews() {

        initializeAdapters()
        loadRestaurantData()

        binding.favoriteButton.setOnClickListener {
            binding.
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initializeAdapters() {

        promotionsAdapter = MenuItemsAdapter()


        binding.rvPromotions.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = promotionsAdapter
        }

        menuAdapter = MenuItemsAdapter()

        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = menuAdapter
        }

    }

    private fun loadRestaurantData() {

        val dto = IdentifierDto(id = restaurant.id)

        RetrofitInstance.getApiService().getRestaurantById(dto)
            .enqueue(object : Callback<ResponseWrapper<Restaurant?>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<Restaurant?>>,
                    response: Response<ResponseWrapper<Restaurant?>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        restaurant = responseData

                        fillRestaurantData()
                        loadMeals()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<Restaurant?>>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun loadMeals() {

        val restaurantId = SessionManager.fetchRestaurantId()!!
        val dto = IdentifierDto(id = restaurantId)

        RetrofitInstance.getApiService().getMeals(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Meal>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Meal>>>,
                    response: Response<ResponseWrapper<List<Meal>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        fillMealsData(responseData)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Meal>>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }

    private fun fillRestaurantData() {

        SessionManager.saveRestaurantId(restaurant.id)

        binding.tvTitle.text = restaurant.name
        binding.collapsingToolbar.title = restaurant.name
        binding.collapsingToolbar.setExpandedTitleColor( ContextCompat.getColor(applicationContext, R.color.none))

        Glide.with(this@RestaurantActivity)
            .load(restaurant.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurant.foodType.name
        binding.chipPrice.text = restaurant.priceRange.getName()

        if (restaurant.rating > 0.0)
            binding.tvRating.text = "${restaurant.rating} rating"
        else
            binding.tvRating.visibility = View.GONE

        binding.infoButton.setOnClickListener {
            openInfoActivity()
        }
    }

    private fun openInfoActivity() {

        val intent = Intent(this@RestaurantActivity, BusinessInfoActivity::class.java)
        intent.putExtra(BusinessProfileFragment.RESTAURANT, restaurant)
        startActivity(intent)
    }

    private fun fillMealsData(meals: List<Meal>) {

        val promotions = meals.filter { meal -> meal.hasPromotion }

        promotionsAdapter.differ.submitList( promotions  )
        menuAdapter.differ.submitList( meals )
        menuAdapter.notifyDataSetChanged()

        if (promotions.isEmpty()) {

            binding.promotionsLayout.visibility = View.GONE

        } else {

            binding.promotionsLayout.visibility = View.VISIBLE
        }

        binding.emptyStateLayout.visibility = if (meals.isEmpty()) View.VISIBLE else View.GONE
    }

}