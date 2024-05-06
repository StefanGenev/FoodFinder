package com.example.foodfinder11.activities


import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.ActivityViewChosenPhotoBinding
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.dto.RegisterRestaurantResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewChosenPhotoActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityViewChosenPhotoBinding

    private lateinit var imageUri: Uri
    private var foodType: FoodType = FoodType()
    private var priceRange: PriceRanges = PriceRanges.CHEAP

    override fun initializeActivity() {
        binding = ActivityViewChosenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        imageUri = Uri.parse(intent.getStringExtra(UploadPhotoActivity.IMAGE_URI))
        foodType = intent.getParcelableExtraProvider<FoodType>(UploadPhotoActivity.FOOD_TYPE) ?: FoodType()
        priceRange = intent.getIntExtra(UploadPhotoActivity.PRICE_RANGE, 0).toEnum<PriceRanges>() ?: PriceRanges.CHEAP
    }
    override fun initializeViews() {

        Glide.with(this@ViewChosenPhotoActivity)
            .load(imageUri)
            .into(binding.chosenPhoto)

        binding.chooseAnotherButton.setOnClickListener {
            finish()
        }
    }

    override fun commitData(): Boolean {

        uploadImage(imageUri)

        return true
    }

    private fun uploadImage(imageUri: Uri) {

        CloudinaryManager.uploadImage(imageUri, object : CloudinaryManager.OnUploadListener {

            override fun onUploadSuccess(imageUrl: String) {
                saveRestaurantRequest(imageUrl)
            }

            override fun onUploadError(error: String) {
                // Handle upload error
                showToast("Upload error: $error")
            }
        })
    }

    private fun saveRestaurantRequest(imageUrl: String) {

        val userData = SessionManager.fetchUserData()

        val dto = RegisterRestaurantRequestDto(0
            , name = userData.name
            , priceRange = priceRange
            , address = ""
            , imageUrl = imageUrl
            , rating = 0.0
            , ownerId = userData.id)

        RetrofitInstance.getApiService().saveRestaurant(dto)
            .enqueue(object : Callback<ResponseWrapper<RegisterRestaurantResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<RegisterRestaurantResponseDto>>,
                    response: Response<ResponseWrapper<RegisterRestaurantResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        SessionManager.saveRestaurantId(responseData.restaurantId)
                        openNextActivity()

                    } else {
                        Toast.makeText(this@ViewChosenPhotoActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<RegisterRestaurantResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ViewChosenPhotoActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun openNextActivity() {

        val intent = Intent(this@ViewChosenPhotoActivity, ChooseAddressActivity::class.java)
        startActivity(intent)

    }
}