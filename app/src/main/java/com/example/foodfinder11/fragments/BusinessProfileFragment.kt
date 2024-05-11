package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.BusinessInfoActivity
import com.example.foodfinder11.activities.CongratulationsActivity
import com.example.foodfinder11.databinding.FragmentBusinessProfileBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.BusinessMainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessProfileFragment : Fragment() {

    companion object {
        const val RESTAURANT = "restaurant"
    }

    private lateinit var binding: FragmentBusinessProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownerId = SessionManager.fetchUserData().id
        loadRestaurantData(ownerId)
    }

    private fun loadRestaurantData(ownerId: Long) {

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
                        fillRestaurantData(responseData)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<Restaurant?>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }

    private fun fillRestaurantData(restaurant: Restaurant) {

        Glide.with(this@BusinessProfileFragment)
            .load(restaurant.imageUrl)
            .into(binding.coverPhoto)

        binding.chipCategory.text = restaurant.foodType.name
        binding.chipPrice.text = restaurant.priceRange.getName()
        binding.tvTitle.text = restaurant.name
        binding.tvRating.text = restaurant.rating.toString()

        binding.infoButton.setOnClickListener {

            val intent = Intent(activity, BusinessInfoActivity::class.java)
            intent.putExtra(RESTAURANT, restaurant)
            startActivity(intent)
        }
    }
}