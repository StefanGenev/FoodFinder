package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterEmailActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityEnterEmailBinding

    companion object {
        const val ENTERED_EMAIL = "entered_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_email)

        binding = ActivityEnterEmailBinding.inflate(layoutInflater)
    }

    fun onContinue(view: View) {
        var checkIfEmailExistsRequestDto =
            CheckIfEmailExistsRequestDto(binding.emailTextInputLayout.editText?.text.toString())

        RetrofitInstance.getApiService().checkIfEmailExists(checkIfEmailExistsRequestDto)
            .enqueue(object : Callback<ResponseWrapper<CheckIfEmailExistsResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    response: Response<ResponseWrapper<CheckIfEmailExistsResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return
                    val responseData = responseBody.data.takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        if (responseData.exists) {



                        } else {

                        }


                    } else {
                        // TODO Log
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    t: Throwable
                ) {
                }
            })
    }

}