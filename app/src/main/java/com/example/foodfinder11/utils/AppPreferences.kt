package com.example.foodfinder11.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodfinder11.R

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        reinitData()
    }

    var token: String?
        get() = Key.TOKEN.getString()
        set(value) = Key.TOKEN.setString(value)

    var refreshToken: String?
        get() = Key.REFRESHTOKEN.getString()
        set(value) = Key.REFRESHTOKEN.setString(value)

    var username: String?
        get() = Key.USERNAME.getString()
        set(value) = Key.USERNAME.setString(value)

    var userRole: Int?
        get() = Key.USER_ROLE.getInt()
        set(value) = Key.USER_ROLE.setInt(value)

    var userEmail: String?
        get() = Key.USER_EMAIL.getString()
        set(value) = Key.USER_EMAIL.setString(value)

    var userId: Long?
        get() = Key.USER_ID.getLong()
        set(value) = Key.USER_ID.setLong(value)

    var restaurantId: Long?
        get() = Key.RESTAURANT_ID.getLong()
        set(value) = Key.RESTAURANT_ID.setLong(value)


    private enum class Key {
        TOKEN,
        REFRESHTOKEN,
        USERNAME,
        USER_ROLE,
        USER_EMAIL,
        USER_ID,
        RESTAURANT_ID;

        fun getBoolean(): Boolean? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(name, false) else null
        fun getFloat(): Float? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getFloat(name, 0f) else null
        fun getInt(): Int? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getInt(name, 0) else null
        fun getLong(): Long? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getLong(name, 0) else null
        fun getString(): String? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null

        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()
        fun setFloat(value: Float?) = value?.let { sharedPreferences!!.edit { putFloat(name, value) } } ?: remove()
        fun setInt(value: Int?) = value?.let { sharedPreferences!!.edit { putInt(name, value) } } ?: remove()
        fun setLong(value: Long?) = value?.let { sharedPreferences!!.edit { putLong(name, value) } } ?: remove()
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun exists(): Boolean = sharedPreferences!!.contains(name)
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }

    fun reinitData() {
        token = ""
        refreshToken = ""
        username = ""
        userRole = 0
        userEmail = ""
        userId = 0
        restaurantId = 0
    }
}