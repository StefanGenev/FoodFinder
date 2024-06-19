package com.example.foodfinder11.activities

import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityFoodTypeInfoBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.dto.SaveFoodTypeRequestDto
import com.example.foodfinder11.dto.SaveFoodTypeResponseDto
import com.example.foodfinder11.fragments.QuestionDialogFragment
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTypeInfoActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityFoodTypeInfoBinding

    private lateinit var foodType: FoodType

    override fun initializeActivity() {
        binding = ActivityFoodTypeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        foodType =
            intent.getParcelableExtraProvider<FoodType>(FoodTypesActivity.FOOD_TYPE)!!
    }

    override fun initializeViews() {

        transferFoodTypeDataToControls()

        binding.deleteButton.setOnClickListener {
            onDelete()
        }

        if (foodType.id <= 0) {
            binding.rightButtonsLayout.visibility = View.GONE

        } else {
            transferFoodTypeDataToControls()
        }
    }

    override fun validateData(): Boolean {

        var result = true

        if (!validateName(binding.nameTextInputLayout, binding.nameTextEdit))
            result = false

        if (!validateName(binding.englishNameTextInputLayout, binding.englishNameTextEdit))
            result = false

        return result
    }

    private fun validateName(inputLayout: TextInputLayout, textEdit: TextInputEditText): Boolean {

        inputLayout.error = ""

        val enteredName = textEdit.text.toString().trim()

        if (enteredName.length < Constants.FOOD_TYPE_NAME_MINIMUM_LENGTH)
        {
            inputLayout.error =
                getString(R.string.food_type_name_must_be_at_least_symbols_long, Constants.MEAL_NAME_MINIMUM_LENGTH.toString())
            inputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            inputLayout.invalidate()

            return false
        }

        return true
    }

    override fun commitData(): Boolean {

        saveFoodTypeRequest()

        return true
    }

    private fun transferFoodTypeDataToControls() {

        binding.nameTextEdit.setText(foodType.name)
        binding.englishNameTextEdit.setText(foodType.nameEnglish)
    }

    private fun onDelete() {

        QuestionDialogFragment(getString(R.string.are_you_sure),
            getString(R.string.yes),
            getString(R.string.no),
            onOkAction = { dialog, id ->

                deleteFoodTypeRequest()
                dialog.dismiss()
            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(supportFragmentManager, "QuestionDialog")
    }

    private fun deleteFoodTypeRequest() {

        val dto = IdentifierDto(id = foodType.id)

        RetrofitInstance.getApiService().deleteFoodType(dto)

            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        Toast.makeText(this@FoodTypeInfoActivity, getString(R.string.food_type_deleted), Toast.LENGTH_SHORT).show()
                        returnOkIntent()

                    } else {
                        Toast.makeText(this@FoodTypeInfoActivity,
                            getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
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

    private fun saveFoodTypeRequest() {

        val dto = SaveFoodTypeRequestDto(id = foodType.id,
                                         name = binding.nameTextEdit.text.toString().trim(),
                                         nameEnglish = binding.englishNameTextEdit.text.toString().trim())

        RetrofitInstance.getApiService().saveFoodType(dto)

            .enqueue(object : Callback<ResponseWrapper<SaveFoodTypeResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<SaveFoodTypeResponseDto>>,
                    response: Response<ResponseWrapper<SaveFoodTypeResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                        returnOkIntent()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<SaveFoodTypeResponseDto>>,
                    t: Throwable
                ) {
                    Log.d("MainViewModel", t.message.toString())
                }
            })
    }


}