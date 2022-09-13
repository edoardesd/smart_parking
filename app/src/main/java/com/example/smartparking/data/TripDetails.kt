package com.example.smartparking.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class TripDetails(
    val navigationMethod: String,
    val navigationText: String,
    val bubbleStops: @RawValue Any? = null
) : Parcelable
