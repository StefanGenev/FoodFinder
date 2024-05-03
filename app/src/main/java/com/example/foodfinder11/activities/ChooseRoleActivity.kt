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

    override fun initializeActivity() {
        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {
        val intent = intent
        enteredEmail = intent.getStringExtra(EnterPasswordActivity.ENTERED_EMAIL)!!
        enteredPassword = intent.getStringExtra(EnterPasswordActivity.ENTERED_PASSWORD)!!
    }

    fun onClickCustomer(view: View) {
        startNextActivity(Roles.CUSTOMER)
    }

    fun onClickBusiness(view: View) {
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