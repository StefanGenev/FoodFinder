package com.example.foodfinder11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityChooseRoleBinding
import com.example.foodfinder11.dto.RegisterRequestDto
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.toInt

class ChooseRoleActivity : BaseNavigatableActivity() {

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val ROLE = "role"
    }

    private lateinit var binding: ActivityChooseRoleBinding

    private var enteredEmail: String = ""
    private var enteredPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_role)

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)

        val intent = intent
        enteredEmail = intent.getStringExtra(EnterPasswordActivity.ENTERED_EMAIL)!!
        enteredPassword = intent.getStringExtra(EnterPasswordActivity.ENTERED_PASSWORD)!!
    }

    private fun onClickCustomer(view: View) {
        startNextActivity(Roles.CUSTOMER)
    }

    private fun onClickBusiness(view: View) {
        startNextActivity(Roles.RESTAURANT)
    }

    private fun startNextActivity(role: Roles) {

        val intent = Intent(this@ChooseRoleActivity, EnterNameActivity::class.java)
        intent.putExtra(EMAIL, enteredEmail)
        intent.putExtra(PASSWORD, enteredPassword)
        intent.putExtra(ROLE, role.toInt())
        startActivity(intent)
    }
}