package com.example.foodfinder11.model

import android.os.Parcelable
import com.example.foodfinder11.dataObjects.Languages
import com.example.foodfinder11.utils.SessionManager
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodType (

    var id: Long = 0,

    var name: String = "",

    var nameEnglish: String = "",

    ) : Parcelable {
        fun getLocalName(): String {

            val selectedLanguage = SessionManager.fetchLanguageLocale()

            if (selectedLanguage == Languages.ENGLISH.getLocale()) {

                return nameEnglish

            } else {
                return name
            }
        }
    }
