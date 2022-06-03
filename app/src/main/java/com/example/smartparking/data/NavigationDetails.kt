package com.example.smartparking.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavigationDetails(
    val room: RoomDetails,
    val time : String
) : Parcelable