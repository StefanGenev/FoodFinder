package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityCongratulationsBinding
import com.example.foodfinder11.databinding.ActivityEnterEmailBinding
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.SessionManager

class CongratulationsActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityCongratulationsBinding

    override fun initializeActivity() {
        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {
        val headerTextView = findViewById<TextView>(R.id.header_title)
        val subtitleTextView = findViewById<TextView>(R.id.subtitle)

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            headerTextView.text = "Registration successful!"
            subtitleTextView.text = "Your profile is ready to use"

        } else {

            headerTextView.text = "Successfully registered!"
            subtitleTextView.text = "As a business, you need to enter some data such as cover photo, food type and location, that the customers will see. You can do so now or login later to fill in the data."
        }
    }

    override fun commitData(): Boolean {

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            val intent = Intent(this@CongratulationsActivity, MainActivity::class.java)
            startActivity(intent)

        } else {

            val intent = Intent(this@CongratulationsActivity, NewBusinessDataActivity::class.java)
            startActivity(intent)

        }

        return true
    }
}