package com.example.foodfinder11

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(
    var idMeal: String? = null,
    var intMealCount: Int,
    var strMealName: String? = null,
    var strMealThumb: String? = null,
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idMeal)
        parcel.writeInt(intMealCount)
        parcel.writeString(strMealName)
        parcel.writeString(strMealThumb)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }

}