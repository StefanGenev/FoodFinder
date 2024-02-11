package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foodfinder11.databinding.ActivitySignUpBinding
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
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
            var registerRequestDto = RegisterRequestDto(binding.email.text.toString(),
                                                        binding.username.text.toString(),
                                                        binding.password.text.toString(),
                                                        Roles.CUSTOMER)

            RetrofitInstance.getApiService().register(registerRequestDto).enqueue(object : Callback<RegisterResponseDto> {
                override fun onResponse(call: Call<RegisterResponseDto>, response: Response<RegisterResponseDto>) {
                    var token = response.body()!!.authToken

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<RegisterResponseDto>, t: Throwable) {

                }
            })
        })

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}