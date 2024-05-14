package com.example.foodfinder11.utils

import android.location.Address
import com.google.android.gms.maps.model.LatLng

object AddressUtils {

    fun getAddressVisualisationText(address: Address): String {
        return "${address.countryName}, ${address.locality}, ${address.thoroughfare}, ${address.subThoroughfare}"
    }

    fun getLatitudeLongitudeFromString(string: String): LatLng {

        val latlong = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val latitude = latlong[0].toDouble()
        val longitude = latlong[1].toDouble()

        return LatLng(latitude, longitude)
    }

    fun getStringFromLatLng(latLng: LatLng): String {
        return "${latLng.latitude}, ${latLng.longitude}"
    }
}