package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterPasswordBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EnterPasswordActivity : BaseNavigatableActivity() {
    companion object {
        const val ENTERED_EMAIL = "entered_email"
        const val ENTERED_PASSWORD = "entered_password"
    }

    private lateinit var binding: ActivityEnterPasswordBinding

    private var userExists: Boolean = false
    private var enteredEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_enter_password)
        binding = ActivityEnterPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun initializeActivity() {

        val headerTextView = findViewById<TextView>(R.id.header_title)
        val subtitleTextView = findViewById<TextView>(R.id.subtitle)

        if (userExists) {
            headerTextView.text = "Welcome back"
            subtitleTextView.text = "Enter password to continue"
        } else {
            headerTextView.text = "Enter Password"
            subtitleTextView.text = "Make sure to use at least one capital letter, special symbol and digit"
        }

    }

    override fun initializeData() {
        val intent = intent
        enteredEmail = intent.getStringExtra(EnterEmailActivity.ENTERED_EMAIL)!!
        userExists = intent.getBooleanExtra(EnterEmailActivity.USER_EXISTS, false)
    }

    override fun commitData(): Boolean {

        val textInputEditText = findViewById<TextInputEditText>(R.id.passwordTextEdit)
        val enteredPassword = textInputEditText.text.toString()

        if (userExists) {

            val loginRequestDto = LoginRequestDto(enteredEmail, enteredPassword)
            loginRequest(loginRequestDto)

        } else {

            val intent = Intent(this@EnterPasswordActivity, ChooseRoleActivity::class.java)
            intent.putExtra(ENTERED_EMAIL, enteredEmail)
            intent.putExtra(ENTERED_PASSWORD, enteredPassword)

            startActivity(intent)
        }

        return true
    }

    fun loginRequest(dto: LoginRequestDto) {

        RetrofitInstance.getApiService().login(dto)
            .enqueue(object : Callback<ResponseWrapper<LoginResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<LoginResponseDto>>,
                    response: Response<ResponseWrapper<LoginResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        SessionManager.saveUserData(responseData.user)
                        startNextActivityOnSucessfullLogin()

                    } else {
                        Toast.makeText(this@EnterPasswordActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<LoginResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EnterPasswordActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun startNextActivityOnSucessfullLogin() {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            val intent = Intent(this@EnterPasswordActivity, MainActivity::class.java)
            startActivity(intent)

        } else {


        }
    }
}