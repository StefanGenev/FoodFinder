package com.example.foodfinder11.activities

import android.content.Intent
import android.view.View
import com.example.foodfinder11.databinding.ActivityWelcomeBinding
import com.example.foodfinder11.utils.AppPreferences
import com.example.foodfinder11.utils.CloudinaryManager


class WelcomeActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun initializeActivity() {
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData(): Boolean {

        AppPreferences.setup(applicationContext)
        CloudinaryManager.startMediaManager(this)

        return true
    }

    override fun initializeViews() {

        binding.emailButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EnterEmailActivity::class.java)
            startActivity(intent)
        })

    }

}