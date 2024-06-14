package com.example.foodfinder11.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import com.example.foodfinder11.R

open class BaseEditText(context: Context, attrs: AttributeSet?) : com.google.android.material.textfield.TextInputEditText(context, attrs) {

    init {
        setTextColor(context.getColor(R.color.black))
        setTextAppearance(R.style.MediumTextAppearance)
        imeOptions = EditorInfo.IME_ACTION_DONE
        maxLines = 1
    }

}