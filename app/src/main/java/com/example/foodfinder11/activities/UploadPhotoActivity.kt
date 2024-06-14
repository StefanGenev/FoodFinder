package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.databinding.ActivityUploadPhotoBinding
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.utils.ImageUtils.createTempPictureUri
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum
import com.example.foodfinder11.utils.toInt


class UploadPhotoActivity : BaseNavigatableActivity() {

    companion object {
        const val IMAGE_URI = "image_uri"
        const val FOOD_TYPE = "food_type"
        const val PRICE_RANGE = "price_range"
        const val PHONE_NUMBER = "phone_number"
    }

    private lateinit var binding: ActivityUploadPhotoBinding
    private lateinit var imageUri: Uri

    private var foodType: FoodType = FoodType()
    private var priceRange: PriceRanges = PriceRanges.CHEAP
    private var phoneNumber: String = ""

    private val uploadActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val imageUri = result.data?.data

            if (imageUri != null) {

                this.imageUri = imageUri
                viewChosenImage()

            } else {

                showToast("Failed to retrieve image URI")
            }
        }
    }

    val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->

        if (success) {
            viewChosenImage()
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
        phoneNumber = intent.getStringExtra(NewBusinessDataActivity.PHONE_NUMBER)!!

    }

    private fun onClickChoosePhoto() {
        chooseImage()
    }

    private fun onClickTakePhoto() {

        imageUri = createTempPictureUri()
        takePicture.launch(imageUri)
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        uploadActivityResultLauncher.launch(intent)
    }

    private fun viewChosenImage() {

        val intent = Intent(this@UploadPhotoActivity, ViewChosenPhotoActivity::class.java)
        intent.putExtra(IMAGE_URI, imageUri.toString())
        intent.putExtra(FOOD_TYPE, foodType)
        intent.putExtra(PRICE_RANGE, priceRange.toInt())
        intent.putExtra(PHONE_NUMBER, phoneNumber)
        startActivity(intent)

    }

}