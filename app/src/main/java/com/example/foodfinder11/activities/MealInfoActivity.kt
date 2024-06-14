package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
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
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface PromotionDialogActivityContract{
    fun setHasPromotion(flag: Boolean)
    fun setPromotionType(type: PromotionTypes)
    fun setPercent(percent: Int)

    fun getHasPromotion(): Boolean
    fun getPromotionType(): PromotionTypes
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
        updateHideButton()
    }

    override fun validateData(): Boolean {

        var result = true

        binding.nameTextInputLayout.error = ""

        val enteredName = binding.nameTextEdit.text.toString().trim()

        if (enteredName.length < Constants.MEAL_NAME_MINIMUM_LENGTH)
        {
            binding.nameTextInputLayout.error =
                getString(R.string.meal_name_must_be_at_least_symbols_long, Constants.MEAL_NAME_MINIMUM_LENGTH.toString())
            binding.nameTextInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.nameTextInputLayout.invalidate()

            result = false
        }

        binding.descriptionInputLayout.error = ""

        val description = binding.descriptionTextEdit.text.toString().trim()

        if (description.length < Constants.MEAL_DESCRIPTION_MINIMUM_LENGTH)
        {
            binding.descriptionInputLayout.error =
                getString(R.string.meal_description_must_be_at_least_symbols_long, Constants.MEAL_DESCRIPTION_MINIMUM_LENGTH.toString())
            binding.descriptionInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.descriptionInputLayout.invalidate()

            result = false
        }

        binding.priceInputLayout.error = ""

        val priceString = binding.priceEditText.text.toString()

        if (priceString.isEmpty() || priceString.toDouble() <= 0.0)
        {
            binding.priceInputLayout.error = getString(R.string.the_price_cannot_be_zero)
            binding.priceInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.priceInputLayout.invalidate()

            result = false
        }

        binding.photoError.visibility = View.GONE
        if (meal.id <= 0 && !imageChanged) {

            binding.photoError.visibility = View.VISIBLE
            result = false
        }

        return result
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

            binding.promotionButton.text = getString(R.string.edit_promotion)
            binding.promotionButton.setBackgroundColor( ContextCompat.getColor(applicationContext, R.color.light_salva) )
            binding.promotionButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.salva))


        } else {

            binding.promotionButton.text = getString(R.string.add_promotion)
            binding.promotionButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_forest))
            binding.promotionButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.forest))
        }
    }

    private fun onDelete() {

        QuestionDialogFragment(getString(R.string.are_you_sure),
            getString(R.string.yes),
            getString(R.string.no),
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
                        Toast.makeText(this@MealInfoActivity,
                            getString(R.string.meal_deleted), Toast.LENGTH_SHORT).show()
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

        val dialogTitle = if (!meal.isHidden) getString(R.string.are_you_sure_this_action_will_hide_the_current_meal_from_your_customers)
                            else getString(R.string.are_you_sure_this_action_will_show_the_current_meals_to_your_customers)

        QuestionDialogFragment(dialogTitle,
            getString(R.string.yes),
            getString(R.string.no),
            onOkAction = { dialog, id ->

                meal.isHidden = !meal.isHidden

                updateHideButton()


            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(supportFragmentManager, "QuestionDialog")
    }

    private fun updateHideButton() {

        val drawable = if (meal.isHidden) R.drawable.show else R.drawable.hide

        binding.hideButton.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                drawable
            )
        )
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
                showToast(getString(R.string.upload_error, error))
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

            meal.promotionPercent = 0
        }
    }

    override fun setPromotionType(type: PromotionTypes) {
        meal.promotionType = type
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