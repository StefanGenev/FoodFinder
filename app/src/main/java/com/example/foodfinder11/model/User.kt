package com.example.foodfinder11.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    var id: Long = 0,

    var name: String = "",

    var email: String = "",

    var role: Roles = Roles.CUSTOMER,

    var favoriteRestaurants: List<Restaurant> = mutableListOf(),

) : Parcelable {
    fun getRoleName(): String {

        var roleName = role.getName()

        return roleName
    }
}