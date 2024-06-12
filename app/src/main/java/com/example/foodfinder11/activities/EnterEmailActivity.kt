package com.example.foodfinder11.activities

import android.content.Intent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.dto.CheckIfEmailExistsRequestDto
import com.example.foodfinder11.dto.CheckIfEmailExistsResponseDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EnterEmailActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityEnterEmailBinding
    companion object {
        const val ENTERED_EMAIL = "entered_email"
        const val USER_EXISTS = "user_exists"
    }

    override fun initializeActivity() {
        binding = ActivityEnterEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        binding.emailTextEdit.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            var handled = false

            // Some phones disregard the IME setting option in the xml, instead
            // they send IME_ACTION_UNSPECIFIED so we need to catch that
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {

                validateData()
                commitData()
            }
            handled
        })

        binding.emailTextEdit.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    override fun commitData(): Boolean {

        val enteredEmail = binding.emailTextEdit.text.toString()

        var checkIfEmailExistsRequestDto =
            CheckIfEmailExistsRequestDto(enteredEmail.trim())

        RetrofitInstance.getApiService().checkIfEmailExists(checkIfEmailExistsRequestDto)
            .enqueue(object : Callback<ResponseWrapper<CheckIfEmailExistsResponseDto>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    response: Response<ResponseWrapper<CheckIfEmailExistsResponseDto>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return

                        val intent = Intent(this@EnterEmailActivity, EnterPasswordActivity::class.java)
                        intent.putExtra(EnterEmailActivity.ENTERED_EMAIL, enteredEmail)
                        intent.putExtra(EnterEmailActivity.USER_EXISTS, responseData.exists)

                        startActivity(intent)

                    } else {
                        Toast.makeText(this@EnterEmailActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<CheckIfEmailExistsResponseDto>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@EnterEmailActivity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })

        return true

    }

}