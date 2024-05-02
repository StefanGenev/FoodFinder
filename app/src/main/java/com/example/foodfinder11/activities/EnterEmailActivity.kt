package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EnterEmailActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityEnterEmailBinding
    companion object {
        const val ENTERED_EMAIL = "entered_email"
        const val USER_EXISTS = "user_exists"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_email)

        binding = ActivityEnterEmailBinding.inflate(layoutInflater)
    }

    override fun commitData(): Boolean {

        val textInputEditText = findViewById<TextInputEditText>(R.id.emailTextEdit)
        val enteredEmail = textInputEditText.text.toString()

        var checkIfEmailExistsRequestDto =
            CheckIfEmailExistsRequestDto(enteredEmail)

        RetrofitInstance.getApiService().checkIfEmailExists(checkIfEmailExistsRequestDto)
            .enqueue(object : Callback<ResponseWrapper<CheckIfEmailExistsResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    response: Response<ResponseWrapper<CheckIfEmailExistsResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        val intent = Intent(this@EnterEmailActivity, EnterPasswordActivity::class.java)
                        intent.putExtra(EnterEmailActivity.ENTERED_EMAIL, enteredEmail)
                        intent.putExtra(EnterEmailActivity.USER_EXISTS, responseData.exists)

                        startActivity(intent)

                    } else {
                        Toast.makeText(this@EnterEmailActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EnterEmailActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })

        return true

    }

}