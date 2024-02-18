package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodfinder11.databinding.ActivityLoginBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.AppPreferences
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.setup(applicationContext)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        sessionManager = SessionManager()

        setContentView(binding.root)

        binding.loginButton.setOnClickListener(View.OnClickListener {

            var loginRequestDto = LoginRequestDto(binding.username.text.toString(), binding.password.text.toString())

            RetrofitInstance.getApiService().login(loginRequestDto).enqueue(object : Callback<ResponseWrapper<LoginResponseDto>> {
                override fun onResponse(call: Call<ResponseWrapper<LoginResponseDto>>, response: Response<ResponseWrapper<LoginResponseDto>>) {
                    val responseBody = response.body().takeIf {it != null} ?: return
                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    if (responseBody.status == 200) {
                        sessionManager.saveAuthToken(responseData.authToken)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // TODO Log
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<LoginResponseDto>>, t: Throwable) {
                }
            })
        })

        binding.signupButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        })
    }
}