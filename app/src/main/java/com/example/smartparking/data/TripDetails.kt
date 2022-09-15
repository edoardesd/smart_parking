package com.example.smartparking.data

import android.os.Parcelable
import com.example.smartparking.internal.TransportMode
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class TripDetails(
    val navigationMethod: TransportMode,
    val navigationText: String,
    val totalTimeTrip: String,
    val parkingAvailability: String,
    val bubbleStops: @RawValue Any? = null
) : Parcelable
