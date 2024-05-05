package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}