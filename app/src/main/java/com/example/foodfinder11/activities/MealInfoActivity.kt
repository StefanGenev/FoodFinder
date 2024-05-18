package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.ActivityMealInfoBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveMealRequestDto
import com.example.foodfinder11.dto.SaveMealResponseDto
import com.example.foodfinder11.fragments.BusinessProfileFragment
import com.example.foodfinder11.fragments.PromotionBottomSheetFragment
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealInfoActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityMealInfoBinding
    private lateinit var promotionBottomSheetDialog: PromotionBottomSheetFragment

    private lateinit var meal: Meal

    private lateinit var imageUri: Uri
    private var imageChanged: Boolean = false

    private val uploadActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val imageUri = result.data?.data

            if (imageUri != null) {

                this.imageUri = imageUri
                imageChanged = true
                viewChosenImage()

            } else {

                showToast("Failed to retrieve image URI")
            }
        }
    }

    override fun initializeActivity() {
        binding = ActivityMealInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        meal =
            intent.getParcelableExtraProvider<Meal>(BusinessProfileFragment.MEAL)!!
    }

    override fun initializeViews() {

        binding.choosePhotoLayout.setOnClickListener {
            choosePhoto()
        }

        binding.nameTextEdit.setText(meal.name)
        binding.descriptionTextEdit.setText(meal.description)
        binding.priceEditText.setText(meal.price.toString())

        binding.promotionButton.setOnClickListener {
            openPromotionBottomSheet()
        }

        Glide.with(this@MealInfoActivity)
            .load(meal.imageUrl)
            .into(binding.menuImage)

        binding.tvUploadPhoto.setText("Upload new photo")
    }

    override fun commitData(): Boolean {

        if( imageChanged ) {

            uploadImage(imageUri)

        } else {
            saveMealRequest()
        }

        return true
    }

    private fun openPromotionBottomSheet() {

        promotionBottomSheetDialog = PromotionBottomSheetFragment()
        promotionBottomSheetDialog.show(supportFragmentManager, "BottomSheetDialog")
    }

    private fun choosePhoto() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        uploadActivityResultLauncher.launch(intent)
    }

    private fun viewChosenImage() {
        Glide.with(this@MealInfoActivity)
            .load(imageUri)
            .into(binding.menuImage)
    }
    private fun uploadImage(imageUri: Uri) {

        CloudinaryManager.uploadImage(imageUri, object : CloudinaryManager.OnUploadListener {

            override fun onUploadSuccess(imageUrl: String) {
                meal.imageUrl = imageUrl
                saveMealRequest()
            }

            override fun onUploadError(error: String) {
                // Handle upload error
                showToast("Upload error: $error")
            }
        })
    }

    private fun saveMealRequest() {

        meal.name = binding.nameTextEdit.text.toString()
        meal.description = binding.descriptionTextEdit.text.toString()
        meal.price = binding.priceEditText.text.toString().toDouble()

        val dto = SaveMealRequestDto( id = meal.id
            , name = meal.name
            , description = meal.description
            , price = meal.price
            , imageUrl = meal.imageUrl
            , restaurantId = SessionManager.fetchRestaurantId()!!)

        RetrofitInstance.getApiService().saveMeal(dto)

            .enqueue(object : Callback<ResponseWrapper<SaveMealResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<SaveMealResponseDto>>,
                    response: Response<ResponseWrapper<SaveMealResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                        returnOkIntent()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<SaveMealResponseDto>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }

}