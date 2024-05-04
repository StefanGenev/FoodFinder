package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

open class BaseNavigatableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeActivity()

        with(window) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        if ( !loadData() ) {
            return
        }

        initializeData()
        initializeViews()
    }

    open fun initializeViews() {
    }

    open fun initializeActivity() {
    }

    open fun initializeData() {
    }
    open fun loadData() : Boolean {
        return true
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