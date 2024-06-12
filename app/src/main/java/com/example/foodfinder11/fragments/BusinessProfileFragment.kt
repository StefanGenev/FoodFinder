package com.example.foodfinder11.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.BaseNavigatableActivity
import com.example.foodfinder11.activities.BusinessInfoActivity
import com.example.foodfinder11.activities.EditBusinessActivity
import com.example.foodfinder11.activities.MealInfoActivity
import com.example.foodfinder11.adapters.MenuItemsAdapter
import com.example.foodfinder11.databinding.FragmentBusinessProfileBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.RestaurantStatuses
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessProfileFragment : Fragment() {

    companion object {
        const val RESTAURANT = "restaurant"
        const val MEAL = "meal"
    }

    private lateinit var binding: FragmentBusinessProfileBinding
    private lateinit var promotionsAdapter: MenuItemsAdapter
    private lateinit var menuAdapter: MenuItemsAdapter

    private var restaurant: Restaurant = Restaurant()

    private val restaurantInfoActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            loadRestaurantData()
        }
    }

    private val mealInfoActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            loadMeals()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewMealButton.setOnClickListener {
            createNewMeal()
        }

        binding.infoButton.setOnClickListener {
            openInfoActivity()
        }

        binding.editButton.setOnClickListener{
            openEditBusinessActivity()
        }

        // set toolbar as support action bar
        (activity as BaseNavigatableActivity).setSupportActionBar(binding.toolbar)

        initializeAdapters()
        loadRestaurantData()
    }

    private fun initializeAdapters() {

        promotionsAdapter = MenuItemsAdapter()

        promotionsAdapter.onItemClicked(object : MenuItemsAdapter.OnMenuItemClicked {

            override fun onClickListener(menuItem: Meal) {
                editMeal(menuItem)
            }

        })

        binding.rvPromotions.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = promotionsAdapter
        }

        menuAdapter = MenuItemsAdapter()

        menuAdapter.onItemClicked(object : MenuItemsAdapter.OnMenuItemClicked {
            override fun onClickListener(menuItem: Meal) {
                editMeal(menuItem)
            }

        })

        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = menuAdapter
        }

    }

    private fun createNewMeal() {
        val intent = Intent(activity, MealInfoActivity::class.java)
        intent.putExtra(MEAL, Meal())
        mealInfoActivityResultLauncher.launch(intent)
    }

    private fun editMeal(meal: Meal) {

        val intent = Intent(activity, MealInfoActivity::class.java)
        intent.putExtra(MEAL, meal)
        mealInfoActivityResultLauncher.launch(intent)
    }

    private fun loadRestaurantData() {

        val ownerId = SessionManager.fetchUserData().id
        val dto = IdentifierDto(id = ownerId)

        RetrofitInstance.getApiService().getByOwnerId(dto)
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

        val restaurantId = SessionManager.fetchRestaurant().id
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

        SessionManager.saveRestaurant(restaurant)

        binding.tvTitle.text = restaurant.name
        binding.collapsingToolbar.title = restaurant.name
        binding.collapsingToolbar.setExpandedTitleColor( ContextCompat.getColor(activity?.applicationContext!!, R.color.none))

        Glide.with(this@BusinessProfileFragment)
            .load(restaurant.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurant.foodType.name
        binding.chipPrice.text = restaurant.priceRange.getName()

        if (restaurant.rating > 0.0)
            binding.tvRating.text = "${restaurant.rating} rating"
        else
            binding.tvRating.visibility = View.GONE

        if (restaurant.status == RestaurantStatuses.HIDDEN) {

            binding.statusInfoLayout.visibility = View.VISIBLE
            binding.statusInfoTitle.text = "Your profile has been hidden."
            binding.statusInfoReason.text = "Reason: ${restaurant.statusNote}\n Please contact support for more information."

        } else if (restaurant.status == RestaurantStatuses.REGISTERED) {

            binding.statusInfoLayout.visibility = View.VISIBLE
            binding.statusInfoTitle.text = "Your profile has not yet been approved, so it won't be shown to customers."
            binding.statusInfoReason.text = "If more than 2 days have passed since registration, please contact support for more information."
        }
    }

    private fun openEditBusinessActivity() {
        val intent = Intent(activity, EditBusinessActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
        restaurantInfoActivityResultLauncher.launch(intent)
    }

    private fun openInfoActivity() {
        val intent = Intent(activity, BusinessInfoActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
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