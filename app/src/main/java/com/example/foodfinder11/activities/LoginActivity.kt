package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.foodfinder11.databinding.ActivityLoginBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
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

            RetrofitInstance.getApiService().login(loginRequestDto).enqueue(object : Callback<LoginResponseDto> {
                override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                    val loginResponse = response.body()

                    if (loginResponse?.statusCode == 200) {
                        sessionManager.saveAuthToken(loginResponse.authToken)
                    } else {
                        // Error logging in
                    }
                }

                override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                }
            })

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        })

        binding.signupButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        })
    }
}