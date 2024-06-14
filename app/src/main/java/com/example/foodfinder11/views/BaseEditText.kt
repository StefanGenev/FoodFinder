package com.example.foodfinder11.views

import android.content.Context
import android.util.AttributeSet
import com.example.foodfinder11.R

open class BaseEditText(context: Context, attrs: AttributeSet?) : com.google.android.material.textfield.TextInputEditText(context, attrs) {

    init {
        setTextColor(context.getColor(R.color.black))
        maxLines = 1
        setTextAppearance(R.style.MediumTextAppearance)
    }

}