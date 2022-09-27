package com.example.smartparking.data


import android.os.Parcelable
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class NavigationDetails(
    val lesson: @RawValue Any? = null,
    val time : String,
    val bubbleStops: @RawValue Any? = null
) : Parcelable