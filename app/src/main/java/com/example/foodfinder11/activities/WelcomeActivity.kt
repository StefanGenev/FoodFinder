package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodfinder11.databinding.ActivityWelcomeBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.AppPreferences
import com.example.foodfinder11.utils.CloudinaryManager
import com.example.foodfinder11.utils.HashingUtils
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.setup(applicationContext)

        with(window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        sessionManager = SessionManager()

        CloudinaryManager.startMediaManager(this)

        setContentView(binding.root)

        binding.emailButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EnterEmailActivity::class.java)
            startActivity(intent)
        })

        /*
        binding.loginButton.setOnClickListener(View.OnClickListener {

            val hashedPassword = HashingUtils.getSHA512(binding.password.text.toString())
            var loginRequestDto = LoginRequestDto(binding.username.text.toString(), hashedPassword)

            RetrofitInstance.getApiService().login(loginRequestDto).enqueue(object : Callback<ResponseWrapper<LoginResponseDto>> {
                override fun onResponse(call: Call<ResponseWrapper<LoginResponseDto>>, response: Response<ResponseWrapper<LoginResponseDto>>) {
                    val responseBody = response.body().takeIf {it != null} ?: return
                    val responseData = responseBody.data.takeIf {it != null} ?: return

                    if (responseBody.status == 200) {
                        sessionManager.saveAuthToken(responseData.accessToken)
                        sessionManager.saveRefreshToken(responseData.token)

                        val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
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
            val intent = Intent(this@WelcomeActivity, SignUpActivity::class.java)
            startActivity(intent)

            //chooseImage()
        })

         */
    }


}