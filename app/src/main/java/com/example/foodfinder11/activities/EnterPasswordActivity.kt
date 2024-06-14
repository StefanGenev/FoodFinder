package com.example.foodfinder11.activities

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityEnterPasswordBinding
import com.example.foodfinder11.dto.LoginRequestDto
import com.example.foodfinder11.dto.LoginResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.ActivityUtils
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.HashingUtils
import com.example.foodfinder11.utils.SessionManager
import com.google.android.material.textfield.TextInputLayout
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

        binding.requirementFive.text = getString(R.string.at_least_symbols, Constants.PASSWORD_MINIMUM_LENGTH.toString())

        initPasswordField()

        if (userExists) {

            binding.headerTitle.text = getString(R.string.welcome_back)
            binding.requirementsLayout.visibility = View.GONE
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        } else {

            binding.headerTitle.text = getString(R.string.enter_password)
            binding.subtitle.visibility = View.GONE
        }

        binding.passwordTextEdit.requestFocus()
    }

    override fun initializeData() {

        val intent = intent
        enteredEmail = intent.getStringExtra(EnterEmailActivity.ENTERED_EMAIL)!!
        userExists = intent.getBooleanExtra(EnterEmailActivity.USER_EXISTS, false)

    }

    override fun validateData(): Boolean {

        binding.passwordInputLayout.error = ""

        val enteredPassword = binding.passwordTextEdit.text.toString().trim()

        if (!validatePassword(enteredPassword))
        {
            setPasswordError(getString(R.string.invalid_password))

            return false
        }

        return true
    }

    private fun setPasswordError(errorMessage: String) {
        binding.passwordInputLayout.error = errorMessage
        binding.passwordInputLayout.invalidate()
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
                        setPasswordError(responseBody.message)
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<LoginResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EnterPasswordActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun startNextActivityOnSucessfullLogin() {
        ActivityUtils.openMainActivityByRole(this@EnterPasswordActivity)
    }

    private fun initPasswordField() {

        binding.passwordTextEdit.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false

            // Some phones disregard the IME setting option in the xml, instead
            // they send IME_ACTION_UNSPECIFIED so we need to catch that
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {

                super.onContinue(binding.passwordTextEdit)
            }
            handled
        })

        binding.passwordTextEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                validatePassword(s.toString())
            }
        })
    }

    private fun validatePassword(password: String): Boolean {

        var result = true

        resetRequirementIcons()

        val hasUppercase = Regex("(.*[A-Z].*)").matches(password)
        if (!hasUppercase) {

            result = false

        } else {
            binding.requirementOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square_check, 0, 0, 0);
        }

        val hasLowercase = Regex("(.*[a-z].*)").matches(password)
        if (!hasLowercase)
        {
            result = false

        } else {
            binding.requirementTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square_check, 0, 0, 0);
        }

        val hasDigit = Regex("(.*[0-9].*)").matches(password)
        if (!hasDigit)
        {
            result = false

        } else {

            binding.requirementThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square_check, 0, 0, 0);
        }

        val hasSpecialSymbol = Regex("(.*[!@#\$%^&*-].*)").matches(password)
        if (!hasSpecialSymbol)
        {
            result = false

        } else {
            binding.requirementFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square_check, 0, 0, 0);
        }

        val isValidLength = password.length >= Constants.PASSWORD_MINIMUM_LENGTH
        if (!isValidLength) {

            result = false

        } else {
            binding.requirementFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square_check, 0, 0, 0);
        }

        return result
    }

    private fun resetRequirementIcons() {
        binding.requirementOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square, 0, 0, 0);
        binding.requirementTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square, 0, 0, 0);
        binding.requirementThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square, 0, 0, 0);
        binding.requirementFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square, 0, 0, 0);
        binding.requirementFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.square, 0, 0, 0);
    }
}