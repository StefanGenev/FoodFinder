package com.example.foodfinder11.activities

import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterNameBinding
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.dto.RegisterResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.Constants.USERNAME_MINIMUM_LENGTH
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.toEnum
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterNameActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityEnterNameBinding

    private var registerRequestDto: RegisterRequestDto = RegisterRequestDto()

    override fun initializeActivity() {
        binding = ActivityEnterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun initializeViews() {

        binding.nameTextEdit.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            // Some phones disregard the IME setting option in the xml, instead
            // they send IME_ACTION_UNSPECIFIED so we need to catch that
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {

                super.onContinue(binding.nameTextEdit)
            }
            handled
        })


        if ( registerRequestDto.role == Roles.CUSTOMER ) {

            binding.subtitle.text = getString(R.string.enter_name_subtitle)

        } else {

            binding.subtitle.text =
                getString(R.string.the_name_that_clients_will_see_along_with_your_profile_picture_in_the_explore_screen)
        }

        binding.nameTextEdit.requestFocus()
    }

    override fun validateData(): Boolean {

        binding.nameTextInputLayout.error = ""

        val enteredName = binding.nameTextEdit.text.toString().trim()

        if (enteredName.length < USERNAME_MINIMUM_LENGTH)
        {
            binding.nameTextInputLayout.error = getString(
                R.string.the_username_must_be_at_least_symbols_long,
                USERNAME_MINIMUM_LENGTH.toString()
            )
            binding.nameTextInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.nameTextInputLayout.invalidate()

            return false
        }

        return true
    }

    override fun commitData(): Boolean {

        registerRequestDto.name = binding.nameTextEdit.text.toString().trim()
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
                    Toast.makeText(this@EnterNameActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun startNextActivity() {
        val intent = Intent(this@EnterNameActivity, CongratulationsActivity::class.java)
        startActivity(intent)
    }
}