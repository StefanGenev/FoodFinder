package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityMealBinding
import com.example.foodfinder11.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)
    }
}