package com.example.smartparking.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RoomDetails(
    val name : String,
    val building : Int,
    val latitude : Double,
    val longitude : Double
) : Parcelable

