package com.example.foodfinder11.activities


import com.example.foodfinder11.databinding.ActivityAboutUsBinding
class AboutUsActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun initializeActivity() {
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}