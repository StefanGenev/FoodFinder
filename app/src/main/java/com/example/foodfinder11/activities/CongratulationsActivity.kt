package com.example.foodfinder11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.foodfinder11.R
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.SessionManager

class CongratulationsActivity : BaseNavigatableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)

        with(window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    override fun initializeActivity() {

        val headerTextView = findViewById<TextView>(R.id.header_title)
        val subtitleTextView = findViewById<TextView>(R.id.subtitle)

        val userData = SessionManager.fetchUserData()

        if ( userData.role == Roles.CUSTOMER ) {

            headerTextView.text = "Registration successful!"
            subtitleTextView.text = "Your profile is ready to use"

        } else {

            headerTextView.text = "Profile successfully registered!"
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