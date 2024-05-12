package com.example.foodfinder11.activities

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

open class BaseNavigatableActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initializeActivity()

        hideSystemUI()

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

    // Function to hide NavigationBar
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun returnOkIntent() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}