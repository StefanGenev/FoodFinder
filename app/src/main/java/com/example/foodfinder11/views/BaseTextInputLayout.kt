package com.example.foodfinder11.views

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.example.foodfinder11.R
import com.google.android.material.textfield.TextInputLayout

@RequiresApi(Build.VERSION_CODES.Q)
open class BaseInputLayout(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {

    init {
        // Set common attributes here
        hintTextColor = ColorStateList.valueOf(context.getColor(R.color.forest))
        boxStrokeColor = context.getColor(R.color.forest)
        cursorColor = ColorStateList.valueOf(context.getColor(R.color.light_forest))
        endIconMode = END_ICON_CLEAR_TEXT
        errorIconDrawable = null
        isCounterEnabled = true
        setCounterTextAppearance(R.style.CounterTextAppearance)
        counterTextColor = ColorStateList.valueOf(context.getColor(R.color.black))
    }

    // You can add additional methods or properties specific to your TextInputLayout usage
}