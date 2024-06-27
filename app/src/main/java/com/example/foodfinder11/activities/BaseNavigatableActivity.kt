package com.example.foodfinder11.activities

import android.R
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.foodfinder11.utils.FullScreenBugWorkaround
import com.example.foodfinder11.utils.RuntimeLocaleChanger

open class BaseNavigatableActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    final override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initializeActivity()
        FullScreenBugWorkaround.assistActivity(this)

        hideSystemUI()

        setLayoutLimitFlags()

        if ( !loadData() ) {
            return
        }

        initializeData()
        initializeViews()
    }

    override fun attachBaseContext(newBase: Context?) {

        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(newBase!!))
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

    open fun setLayoutLimitFlags() {
    }

    open fun initializeActivity() {
    }

    open fun initializeViews() {
    }

    open fun initializeData() {
    }
    open fun loadData() : Boolean {
        return true
    }

    fun onContinue(view: View) {

        if ( !validateData() ) {
            return
        }

        if ( !commitData() ) {
            return
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

    fun returnOkIntent() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}