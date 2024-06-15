package com.example.foodfinder11.activities

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.ReviewsAdapter
import com.example.foodfinder11.databinding.ActivityReviewsBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Review
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityReviewsBinding
    private var reviews: ArrayList<Review> = ArrayList()

    private lateinit var reviewsAdapter: ReviewsAdapter


    override fun initializeActivity() {
        binding = ActivityReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData(): Boolean {

        val role = SessionManager.fetchUserData().role

        if (role == Roles.RESTAURANT) {

            loadReviewsByRestaurantRequest()

        } else {

            binding.tvAverage.visibility = View.GONE
            loadReviewsByUserRequest()
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun loadReviewsByRestaurantRequest() {

        val dto = IdentifierDto(id = SessionManager.fetchRestaurantDetails().restaurant.id)

        RetrofitInstance.getApiService().getReviewsByRestaurantId(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Review>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Review>>>,
                    response: Response<ResponseWrapper<List<Review>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        reviews.addAll(responseData)

                        initReviews()

                    } else {
                        Toast.makeText(this@ReviewsActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Review>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ReviewsActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadReviewsByUserRequest() {

        val dto = IdentifierDto(id = SessionManager.fetchUserData().id)

        RetrofitInstance.getApiService().getReviewsByUserId(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Review>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Review>>>,
                    response: Response<ResponseWrapper<List<Review>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        reviews.addAll(responseData)

                        initReviews()

                    } else {
                        Toast.makeText(this@ReviewsActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Review>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ReviewsActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun initReviews() {

        binding.emptyStateLayout.visibility = if (reviews.isEmpty()) View.VISIBLE else View.GONE

        val averageRating = reviews.sumOf { it.rating }.toDouble()/reviews.size
        binding.tvAverage.text = getString(R.string.average, averageRating.toString())

        reviewsAdapter = ReviewsAdapter()

        resetAdapters()
        reviewsAdapter.differ.submitList( reviews )
    }

    private fun resetAdapters() {

        binding.rvReviews.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = reviewsAdapter
        }
    }

}