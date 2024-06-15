package com.example.foodfinder11.dataObjects

import android.content.Context
import com.example.foodfinder11.R

enum class Languages {

    ENGLISH {
        override fun getName(context: Context): String {
            return context.getString(R.string.english_name)
        }

        override fun getLocale(): String {
            return "en-US"
        }
    },

    BULGARIAN {
        override fun getName(context: Context): String {
            return context.getString(R.string.bulgarian_name)
        }

        override fun getLocale(): String {
            return "bg"
        }
    };

    abstract fun getName(context: Context): String
    abstract fun getLocale(): String
}