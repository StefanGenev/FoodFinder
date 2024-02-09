package com.example.foodfinder11

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class OrderItem(
    var idMeal: Long,
    var intMealCount: Int,
    var strMealName: String? = null,
    var image: ByteArray? = null,
) :
    Parcelable {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readBlob()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(idMeal)
        parcel.writeInt(intMealCount)
        parcel.writeString(strMealName)
        parcel.writeBlob(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }

}