package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterPasswordBinding
import com.example.foodfinder11.databinding.ActivityUploadPhotoBinding
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum

class UploadPhotoActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding

    private var foodType: FoodType = FoodType()
    private var priceRange: PriceRanges = PriceRanges.CHEAP

    private val uploadActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                uploadImage(imageUri)
            } else {
                showToast("Failed to retrieve image URI")
            }
        }
    }

    override fun initializeActivity() {
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        binding.choosePhotoLayout.setOnClickListener {
            onClickChoosePhoto()
        }

        binding.takePhotoLayout.setOnClickListener {
            onClickTakePhoto()
        }
    }

    override fun initializeData() {

        val intent = intent
        foodType = intent.getParcelableExtraProvider<FoodType>(NewBusinessDataActivity.FOOD_TYPE) ?: FoodType()
        priceRange = intent.getIntExtra(NewBusinessDataActivity.PRICE_RANGE, 0).toEnum<PriceRanges>() ?: PriceRanges.CHEAP

    }

    private fun onClickChoosePhoto() {
        chooseImage()
    }

    private fun onClickTakePhoto() {
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        uploadActivityResultLauncher.launch(intent)
    }

    private fun uploadImage(imageUri: Uri) {
        CloudinaryManager.uploadImage(imageUri, object : CloudinaryManager.OnUploadListener {
            override fun onUploadSuccess(imageUrl: String) {
                // Handle successful upload (e.g., save the image URL to your database)
                showToast("Image uploaded successfully. URL: $imageUrl")
            }

            override fun onUploadError(error: String) {
                // Handle upload error
                showToast("Upload error: $error")
            }
        })
    }

}