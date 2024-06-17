package com.example.foodfinder11.activities


import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityReviewBinding
import com.example.foodfinder11.dto.AddReviewRequestDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityReviewBinding

    private lateinit var restaurant: Restaurant
    private var rating: Int = 0

    companion object {
        const val RESTAURANT = "restaurant"
    }

    override fun initializeActivity() {
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(RESTAURANT)!!
    }

    override fun initializeViews() {

        super.initializeViews()

        binding.floatingActionButton.setOnClickListener {
            ActivityUtils.openMainActivityByRole(this@ReviewActivity)
        }

        binding.headerTitle.text = restaurant.name

        Glide.with(this@ReviewActivity)
            .load(restaurant.imageUrl)
            .into(binding.restaurantImage)

        showRatingBar(binding.ratingButton1,
                      binding.ratingButton2,
                      binding.ratingButton3,
                      binding.ratingButton4,
                      binding.ratingButton5)

        binding.ratingButton5.callOnClick()
        binding.ratingButton5.callOnClick()
    }

    override fun commitData(): Boolean {

        addReviewRequest()

        return true
    }

    private fun addReviewRequest() {

        val feedback = binding.feedbackEditText.text.toString().trim()

        val dto = AddReviewRequestDto(  userId = SessionManager.fetchUserData().id,
                                        restaurantId = restaurant.id,
                                        rating = this.rating,
                                        feedback = feedback )

        RetrofitInstance.getApiService().addReview(dto)
            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                        ActivityUtils.openMainActivityByRole(this@ReviewActivity)

                    } else {
                        Toast.makeText(this@ReviewActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ReviewActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun showRatingBar(
        mButton1: MaterialButton,
        mButton2: MaterialButton,
        mButton3: MaterialButton,
        mButton4: MaterialButton,
        mButton5: MaterialButton

    ) {

        mButton1.setOnClickListener { v: View? ->

            mButton5.isChecked = false
            mButton4.isChecked = false
            mButton3.isChecked = false
            mButton2.isChecked = false
            mButton1.isChecked = true

            rating = 1
        }

        mButton2.setOnClickListener { v: View? ->

            mButton5.isChecked = false
            mButton4.isChecked = false
            mButton3.isChecked = false
            mButton1.isChecked = true

            rating = 2
        }

        mButton3.setOnClickListener { v: View? ->

            mButton5.isChecked = false
            mButton4.isChecked = false
            mButton2.isChecked = true
            mButton1.isChecked = true

            rating = 3
        }

        mButton4.setOnClickListener { v: View? ->

            mButton5.isChecked = false
            mButton3.isChecked = true
            mButton2.isChecked = true
            mButton1.isChecked = true

            rating = 4
        }

        mButton5.setOnClickListener { v: View? ->

            mButton4.isChecked = true
            mButton3.isChecked = true
            mButton2.isChecked = true
            mButton1.isChecked = true

            rating = 5
        }
    }
}