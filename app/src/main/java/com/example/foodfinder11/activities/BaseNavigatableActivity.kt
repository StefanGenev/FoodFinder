package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager

open class BaseNavigatableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        loadData()
        initializeData()
        initializeActivity()
    }

    open fun initializeActivity() {
    }

    open fun initializeData() {
    }
    open fun loadData() {
    }

    fun onContinue(view: View) {

        if ( !validateData() ) {
        }

        if ( !commitData() ) {
        }
    }

    open fun validateData(): Boolean {
        return true
    }

    open fun commitData() : Boolean {
        return true
    }

    fun onGoBack(view: View) {
        finish()
    }
}