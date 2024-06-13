package com.example.foodfinder11.model

import android.content.Context
import com.example.foodfinder11.R

enum class Roles {

    CUSTOMER {
        override fun getName(context: Context): String {
            return context.getString(R.string.customer)
        }
    },

    RESTAURANT {
        override fun getName(context: Context): String {
            return context.getString(R.string.owner)
        }
    },

    ADMIN {
        override fun getName(context: Context): String {
            return context.getString(R.string.admin)
        }
    };

    abstract fun getName(context: Context): String
}