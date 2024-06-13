package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.databinding.ActivityWelcomeBinding
import com.example.foodfinder11.utils.AppPreferences
import com.example.foodfinder11.utils.CloudinaryManager


class WelcomeActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private val startLanguagesActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                recreate()
            }
        }

    override fun initializeActivity() {

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CloudinaryManager.startMediaManager(this)
    }

    final override fun attachBaseContext(newBase: Context?) {
        AppPreferences.setup(newBase!!)
        super.attachBaseContext(newBase)
    }

    override fun initializeViews() {

        binding.emailButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EnterEmailActivity::class.java)
            startActivity(intent)
        })

        binding.languageButton.setOnClickListener {
            val intent = Intent(this, ChangeLanguageActivity::class.java)
            startLanguagesActivityForResult.launch(intent)
        }

    }

}