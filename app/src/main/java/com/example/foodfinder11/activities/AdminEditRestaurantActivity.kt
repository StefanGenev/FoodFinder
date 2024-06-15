package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityAdminEditRestaurantBinding
import com.example.foodfinder11.dto.ChangeRestaurantStatusRequestDto
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.fragments.QuestionDialogFragment
import com.example.foodfinder11.model.Restaurant
import com.example.foodfinder11.model.RestaurantStatuses
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.getParcelableExtraProvider
import com.example.foodfinder11.utils.toEnum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditRestaurantActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityAdminEditRestaurantBinding
    private lateinit var restaurant: Restaurant

    private var selectedStatus: RestaurantStatuses = RestaurantStatuses.REGISTERED

    private val startStatusesActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            val intent = result.data
            selectedStatus = intent?.getIntExtra(RestaurantStatusesActivity.STATUS, 0)?.toEnum<RestaurantStatuses>()!!

            binding.statusTextEdit.setText(selectedStatus.getName(binding.statusTextEdit.context))
        }
    }

    companion object {
        const val RESTAURANT = "restaurant"
    }

    override fun initializeActivity() {
        binding = ActivityAdminEditRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        restaurant =
            intent.getParcelableExtraProvider<Restaurant>(RESTAURANT)!!
    }

    override fun initializeViews() {

        binding.statusTextEdit.setText(restaurant.status.getName(binding.statusTextEdit.context))
        binding.noteTextEdit.setText(restaurant.statusNote)

        binding.statusTextEdit.setOnClickListener {
            onClickStatus()
        }

        binding.deleteButton.setOnClickListener {
            onDelete()
        }
    }

    override fun commitData(): Boolean {

        changeStatusRequest()

        return true
    }

    private fun onDelete() {

        QuestionDialogFragment(
            getString(R.string.are_you_sure),
            getString(R.string.yes),
            getString(R.string.no),
            onOkAction = { dialog, id ->

                deleteRequest()
                dialog.dismiss()
            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(supportFragmentManager, "QuestionDialog")
    }

    private fun deleteRequest() {

        val dto = IdentifierDto(id = restaurant.id)

        RetrofitInstance.getApiService().deleteRestaurant(dto)

            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        Toast.makeText(this@AdminEditRestaurantActivity,
                            getString(R.string.restaurant_deleted), Toast.LENGTH_SHORT).show()
                        returnOkIntent()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@AdminEditRestaurantActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun changeStatusRequest() {

        val dto = ChangeRestaurantStatusRequestDto(
            restaurantId = restaurant.id,
            status = selectedStatus,
            note = binding.noteTextEdit.text.toString()
        )

        RetrofitInstance.getApiService().changeRestaurantStatus(dto)
            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {
                       finish()

                    } else {
                        Toast.makeText(
                            this@AdminEditRestaurantActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@AdminEditRestaurantActivity,
                        getString(R.string.problem_with_request),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    fun onClickStatus() {

        val intent = Intent(this@AdminEditRestaurantActivity, RestaurantStatusesActivity::class.java)
        startStatusesActivityForResult.launch(intent)
    }
}