package com.example.foodfinder11.activities


import com.example.foodfinder11.R

class MainActivity : BaseMainActivity() {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val bottomNavigationResourceId: Int
        get() = R.id.bottom_nav

}