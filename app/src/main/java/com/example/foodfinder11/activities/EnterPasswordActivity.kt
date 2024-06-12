package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterPasswordBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.HashingUtils
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.BusinessMainViewModel
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

    override fun initializeActivity() {
        binding = ActivityEnterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        if (userExists) {
            binding.headerTitle.text = "Welcome back"
            binding.subtitle.text = "Enter password to continue"
        } else {
            binding.headerTitle.text = "Enter Password"
            binding.subtitle.text = "Make sure to use at least one capital letter, special symbol and digit"
        }

        binding.passwordTextEdit.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            // Some phones disregard the IME setting option in the xml, instead
            // they send IME_ACTION_UNSPECIFIED so we need to catch that
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {

                validateData()
                commitData()
            }
            handled
        })

        binding.passwordTextEdit.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    override fun initializeData() {

        val intent = intent
        enteredEmail = intent.getStringExtra(EnterEmailActivity.ENTERED_EMAIL)!!
        userExists = intent.getBooleanExtra(EnterEmailActivity.USER_EXISTS, false)

    }

    override fun commitData(): Boolean {

        val enteredPassword = binding.passwordTextEdit.text.toString().trim()
        val hashedPassword = HashingUtils.getSHA512(enteredPassword)

        if (userExists) {

            val loginRequestDto = LoginRequestDto(enteredEmail, hashedPassword)
            loginRequest(loginRequestDto)

        } else {

            val intent = Intent(this@EnterPasswordActivity, ChooseRoleActivity::class.java)
            intent.putExtra(ENTERED_EMAIL, enteredEmail)
            intent.putExtra(ENTERED_PASSWORD, hashedPassword)

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

                        SessionManager.saveAuthToken(responseData.accessToken)
                        SessionManager.saveRefreshToken(responseData.token)
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
        ActivityUtils.openMainActivityByRole(this@EnterPasswordActivity)
    }
}