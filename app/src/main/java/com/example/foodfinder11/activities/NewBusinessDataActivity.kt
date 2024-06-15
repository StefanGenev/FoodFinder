package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityNewBusinessDataBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toInt
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewBusinessDataActivity : BaseNavigatableActivity() {

    companion object {
        const val FOOD_TYPES = "food_types"
        const val FOOD_TYPE = "food_type"
        const val PRICE_RANGE = "price_range"
        const val PHONE_NUMBER = "phone_number"
    }

    private lateinit var binding: ActivityNewBusinessDataBinding
    private var foodTypes: ArrayList<FoodType> = ArrayList()
    private var selectedFoodType: FoodType = FoodType()
    private var selectedPriceRange: PriceRanges = PriceRanges.CHEAP

    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            selectedFoodType = intent?.getParcelableExtraProvider<FoodType>(FoodTypesActivity.FOOD_TYPE)
                ?: FoodType()

            binding.foodTypeTextEdit.setText(selectedFoodType.name)
            binding.foodTypeTextField.error = ""
        }
    }

    override fun initializeActivity() {

        binding = ActivityNewBusinessDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun initializeViews() {

        binding.chipCheap.setOnClickListener {
            selectedPriceRange = PriceRanges.CHEAP
        }

        binding.chipMedium.setOnClickListener {
            selectedPriceRange = PriceRanges.MIDRANGE
        }

        binding.chipExpensive.setOnClickListener {
            selectedPriceRange = PriceRanges.EXPENSIVE
        }

    }

    override fun loadData(): Boolean {

        loadFoodTypesRequest()

        return true
    }

    override fun validateData(): Boolean {

        var result = true

        binding.foodTypeTextField.error = ""

        if (selectedFoodType.name.isEmpty()) {

            binding.foodTypeTextField.error =
                getString(R.string.you_must_choose_food_type_before_continuing)
            binding.foodTypeTextField.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.foodTypeTextField.invalidate()

            result = false
        }

        binding.phoneNumberTextInputLayout.error = ""

        val phoneNumber = binding.phoneNumberTextEdit.text ?: ""

        if (phoneNumber.isEmpty()) {

            binding.phoneNumberTextInputLayout.error = getString(R.string.no_phone_number_entered)
            binding.phoneNumberTextInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.phoneNumberTextInputLayout.invalidate()

            result = false
        }

        return result
    }

    override fun commitData(): Boolean {

        val intent = Intent(this@NewBusinessDataActivity, UploadPhotoActivity::class.java)
        intent.putExtra(FOOD_TYPE, selectedFoodType)
        intent.putExtra(PRICE_RANGE, selectedPriceRange.toInt())

        val phoneNumber = binding.phoneNumberTextEdit.text.toString() ?: ""
        intent.putExtra(PHONE_NUMBER, phoneNumber)

        startActivity(intent)
        
        return true
    }

    fun loadFoodTypesRequest() {

        RetrofitInstance.getApiService().getAllFoodTypes()
            .enqueue(object : Callback<ResponseWrapper<List<FoodType>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    response: Response<ResponseWrapper<List<FoodType>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        foodTypes.addAll(responseData)

                    } else {
                        Toast.makeText(this@NewBusinessDataActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<FoodType>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@NewBusinessDataActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun onClickFoodType(view: View) {

        val intent = Intent(this@NewBusinessDataActivity, FoodTypesActivity::class.java)
        intent.putExtra(FOOD_TYPES, foodTypes)
        startActivityForResult.launch(intent)
    }
}