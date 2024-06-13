package com.example.foodfinder11.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale


object RuntimeLocaleChanger {

    fun wrapContext(context: Context): Context {

        val savedLocale = SessionManager.fetchLanguageLocale()

        val locale = Locale(savedLocale)
        Locale.setDefault(locale)

        // create new configuration with the saved locale
        val newConfig = Configuration()
        newConfig.setLocale(locale)

        return context.createConfigurationContext(newConfig)
    }
}