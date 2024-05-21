package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityMealInfoBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveMealRequestDto
import com.example.foodfinder11.dto.SaveMealResponseDto
import com.example.foodfinder11.fragments.BusinessProfileFragment
import com.example.foodfinder11.fragments.PromotionBottomSheetFragment
import com.example.foodfinder11.fragments.QuestionDialogFragment
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.PromotionTypes
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface PromotionDialogActivityContract{
    fun setHasPromotion(flag: Boolean)
    fun setPromotionType(type: PromotionTypes)
    fun setAdditionalMeals(count: Int)
    fun setPercent(percent: Int)

    fun getHasPromotion(): Boolean
    fun getPromotionType(): PromotionTypes
    fun getAdditionalMeals(): Int
    fun getPercent(): Int

    fun onConfirmPromotion()
    fun onRemovePromotion()
}

class MealInfoActivity : BaseNavigatableActivity(), PromotionDialogActivityContract {

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

        binding.deleteButton.setOnClickListener {
            onDelete()
        }

        binding.hideButton.setOnClickListener {
            onHide()
        }

        binding.choosePhotoLayout.setOnClickListener {
            choosePhoto()
        }

        binding.promotionButton.setOnClickListener {
            openPromotionBottomSheet()
        }

        if (meal.id <= 0) {
            binding.rightButtonsLayout.visibility = View.GONE

        } else {
            transferMealDataToControls()
        }

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

    private fun transferMealDataToControls() {

        binding.nameTextEdit.setText(meal.name)
        binding.descriptionTextEdit.setText(meal.description)
        binding.priceEditText.setText(meal.price.toString())

        initPromotionViews()

        Glide.with(this@MealInfoActivity)
            .load(meal.imageUrl)
            .into(binding.menuImage)
    }

    private fun initPromotionViews() {

        if (meal.hasPromotion) {

            binding.promotionButton.text = "Edit promotion"
            binding.promotionButton.setBackgroundColor( ContextCompat.getColor(applicationContext, R.color.light_salva) )
            binding.promotionButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.salva))


        } else {

            binding.promotionButton.text = "Add promotion"
            binding.promotionButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_forest))
            binding.promotionButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.forest))
        }
    }

    private fun onDelete() {

        QuestionDialogFragment("Are you sure?",
            "Yes",
            "No",
            onOkAction = { dialog, id ->

                deleteMealRequest()
                dialog.dismiss()
            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(supportFragmentManager, "QuestionDialog")
    }

    private fun deleteMealRequest() {

        val dto = IdentifierDto(id = meal.id)

        RetrofitInstance.getApiService().deleteMeal(dto)

            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                        Toast.makeText(this@MealInfoActivity, "Meal deleted", Toast.LENGTH_SHORT).show()
                        returnOkIntent()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }

    private fun onHide() {

        val dialogTitle = if (!meal.isHidden) "Are you sure? This action will hide the current meal from your customers"
                            else "Are you sure? This action will show the current meals to your customers"

        QuestionDialogFragment(dialogTitle,
            "Yes",
            "No",
            onOkAction = { dialog, id ->

                meal.isHidden = !meal.isHidden
            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(supportFragmentManager, "QuestionDialog")
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

        val dto = SaveMealRequestDto(meal)

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

    override fun setHasPromotion(flag: Boolean) {

        meal.hasPromotion = flag

        if (!flag) {

            meal.additionalMealsCount = 0
            meal.promotionPercent = 0
        }
    }

    override fun setPromotionType(type: PromotionTypes) {
        meal.promotionType = type
    }

    override fun setAdditionalMeals(count: Int) {
       meal.additionalMealsCount = count
    }

    override fun setPercent(percent: Int) {
        meal.promotionPercent = percent
    }

    override fun getHasPromotion(): Boolean {
        return meal.hasPromotion
    }

    override fun getPromotionType(): PromotionTypes {
        return meal.promotionType
    }

    override fun getAdditionalMeals(): Int {
        return meal.additionalMealsCount
    }

    override fun getPercent(): Int {
        return meal.promotionPercent
    }

    override fun onConfirmPromotion() {

        initPromotionViews()
    }

    override fun onRemovePromotion() {

        initPromotionViews()
    }

}