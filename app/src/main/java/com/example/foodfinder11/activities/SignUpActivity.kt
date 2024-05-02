package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foodfinder11.databinding.ActivitySignUpBinding
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.HashingUtils
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.signupButton.setOnClickListener(View.OnClickListener {
            val hashedPassword = HashingUtils.getSHA512(binding.password.text.toString())
            var registerRequestDto = RegisterRequestDto(binding.email.text.toString(),
                                                        binding.username.text.toString(),
                                                        hashedPassword,
                                                        Roles.CUSTOMER)

            RetrofitInstance.getApiService().register(registerRequestDto).enqueue(object : Callback<ResponseWrapper<RegisterResponseDto>> {
                override fun onResponse(call: Call<ResponseWrapper<RegisterResponseDto>>, response: Response<ResponseWrapper<RegisterResponseDto>>) {
                    val responseBody = response.body().takeIf {it != null} ?: return
                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    if (responseBody.status == 200) {
                        SessionManager.saveAuthToken(responseData.accessToken)
                        SessionManager.saveRefreshToken(responseData.token)

                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        //TODO Log
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<RegisterResponseDto>>, t: Throwable) {

                }
            })
        })

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SignUpActivity, WelcomeActivity::class.java)
            startActivity(intent)
        })
    }
}