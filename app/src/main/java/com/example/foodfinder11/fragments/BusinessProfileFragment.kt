package com.example.foodfinder11.fragments

import android.app.Activity
import android.content.Intent
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
import com.example.foodfinder11.dto.RestaurantDetailsResponseDto
import com.example.foodfinder11.model.Meal
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

    private var restaurantDetails: RestaurantDetailsResponseDto = RestaurantDetailsResponseDto()

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
            .enqueue(object : Callback<ResponseWrapper<RestaurantDetailsResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<RestaurantDetailsResponseDto>>,
                    response: Response<ResponseWrapper<RestaurantDetailsResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        restaurantDetails = responseData

                        fillRestaurantData()
                        loadMeals()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<RestaurantDetailsResponseDto>>,
                    t: Throwable
                ) {
                }
            })
    }

    private fun loadMeals() {

        val restaurantId = SessionManager.fetchRestaurantDetails().restaurant.id
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

        SessionManager.saveRestaurantDetails(restaurantDetails)

        val restaurant = restaurantDetails.restaurant

        binding.tvTitle.text = restaurant.name
        binding.collapsingToolbar.title = restaurant.name
        binding.collapsingToolbar.setExpandedTitleColor( ContextCompat.getColor(activity?.applicationContext!!, R.color.none))

        Glide.with(this@BusinessProfileFragment)
            .load(restaurant.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurant.foodType.getLocalName()
        binding.chipPrice.text = restaurant.priceRange.getName(binding.chipPrice.context)

        if (restaurantDetails.averageRating > 0.0)
            binding.tvRating.text = "${restaurantDetails.averageRating} ${getString(R.string.rating)}"
        else
            binding.tvRating.visibility = View.GONE

        if (restaurantDetails.totalOrders > 0)
            binding.tvOrders.text = "${restaurantDetails.totalOrders} ${getString(R.string.orders_small)}"
        else
            binding.tvOrders.visibility = View.GONE

        if (restaurant.status == RestaurantStatuses.HIDDEN) {

            binding.statusInfoLayout.visibility = View.VISIBLE
            binding.statusInfoTitle.text = getString(R.string.your_profile_has_been_hidden)
            binding.statusInfoReason.text = getString(
                R.string.reason_please_contact_support_for_more_information,
                restaurant.statusNote
            )

        } else if (restaurant.status == RestaurantStatuses.REGISTERED) {

            binding.statusInfoLayout.visibility = View.VISIBLE
            binding.statusInfoTitle.text =
                getString(R.string.your_profile_has_not_yet_been_approved_so_it_won_t_be_shown_to_customers)
            binding.statusInfoReason.text =
                getString(R.string.if_more_than_2_days_have_passed_since_registration_please_contact_support_for_more_information)
        }
    }

    private fun openEditBusinessActivity() {
        val intent = Intent(activity, EditBusinessActivity::class.java)
        intent.putExtra(RESTAURANT, restaurantDetails.restaurant)
        restaurantInfoActivityResultLauncher.launch(intent)
    }

    private fun openInfoActivity() {
        val intent = Intent(activity, BusinessInfoActivity::class.java)
        intent.putExtra(RESTAURANT, restaurantDetails.restaurant)
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