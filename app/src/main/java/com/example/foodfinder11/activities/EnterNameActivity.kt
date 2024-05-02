package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterNameBinding
import com.example.foodfinder11.databinding.ActivityEnterPasswordBinding
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.toEnum
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterNameActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityEnterNameBinding

    private var registerRequestDto: RegisterRequestDto = RegisterRequestDto()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_name)

        binding = ActivityEnterNameBinding.inflate(layoutInflater)
    }

    override fun initializeData() {
        super.initializeData()

        val intent = intent
        val email = intent.getStringExtra(ChooseRoleActivity.EMAIL)!!
        val password = intent.getStringExtra(ChooseRoleActivity.PASSWORD)!!
        val role = intent.getIntExtra(ChooseRoleActivity.ROLE, 0).toEnum<Roles>()!!

        registerRequestDto.email = email
        registerRequestDto.password = password
        registerRequestDto.role = role
    }

    override fun commitData(): Boolean {

        val textInputEditText = findViewById<TextInputEditText>(R.id.nameTextEdit)
        registerRequestDto.name = textInputEditText.text.toString()

        registerRequest()

        return true
    }

    private fun registerRequest() {

        RetrofitInstance.getApiService().register(registerRequestDto)
            .enqueue(object : Callback<ResponseWrapper<RegisterResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<RegisterResponseDto>>,
                    response: Response<ResponseWrapper<RegisterResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        SessionManager.saveAuthToken(responseData.accessToken)
                        SessionManager.saveRefreshToken(responseData.token)
                        SessionManager.saveUserData(responseData.user)

                        startNextActivity()

                    } else {
                        Toast.makeText(this@EnterNameActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<RegisterResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EnterNameActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startNextActivity() {
        val intent = Intent(this@EnterNameActivity, CongratulationsActivity::class.java)
        startActivity(intent)
    }
}