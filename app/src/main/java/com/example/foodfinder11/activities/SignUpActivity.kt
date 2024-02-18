package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foodfinder11.databinding.ActivitySignUpBinding
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        sessionManager = SessionManager()

        setContentView(binding.root)

        binding.signupButton.setOnClickListener(View.OnClickListener {
            var registerRequestDto = RegisterRequestDto(binding.email.text.toString(),
                                                        binding.username.text.toString(),
                                                        binding.password.text.toString(),
                                                        Roles.CUSTOMER)

            RetrofitInstance.getApiService().register(registerRequestDto).enqueue(object : Callback<ResponseWrapper<RegisterResponseDto>> {
                override fun onResponse(call: Call<ResponseWrapper<RegisterResponseDto>>, response: Response<ResponseWrapper<RegisterResponseDto>>) {
                    val responseBody = response.body().takeIf {it != null} ?: return
                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    if (responseBody.status == 200) {
                        sessionManager.saveAuthToken(responseData.authToken)

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
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}