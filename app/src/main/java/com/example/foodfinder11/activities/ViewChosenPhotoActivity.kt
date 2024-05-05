package com.example.foodfinder11.activities


import android.content.Intent
import android.net.Uri
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.ActivityViewChosenPhotoBinding
import com.example.foodfinder11.dto.RegisterRestaurantRequestDto
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum

class ViewChosenPhotoActivity : BaseNavigatableActivity() {

    companion object {
        const val REGISTER_RESTAURANT_DTO = "register_restaurant_dto"
    }

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
                openNextActivity(imageUrl)
            }

            override fun onUploadError(error: String) {
                // Handle upload error
                showToast("Upload error: $error")
            }
        })
    }

    private fun openNextActivity(imageUrl: String) {

        val userData = SessionManager.fetchUserData()
        val registerDto = RegisterRestaurantRequestDto(userData.name, priceRange, imageUrl, "", userData.id)

        val intent = Intent(this@ViewChosenPhotoActivity, ChooseAddressActivity::class.java)
        intent.putExtra(REGISTER_RESTAURANT_DTO, registerDto)
        startActivity(intent)
    }
}