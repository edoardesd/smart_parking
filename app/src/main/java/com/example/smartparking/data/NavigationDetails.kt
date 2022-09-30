package com.example.smartparking.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class NavigationDetails(
    val lesson: @RawValue Any? = null,
    val time : @RawValue Any? = null,
    val bubbleStops: @RawValue Any? = null
) : Parcelable