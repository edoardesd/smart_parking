package com.example.smartparking.data

import android.os.Parcelable
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.ArrayList

@Parcelize
data class TripDetails(
    val infoNavigation: @RawValue Any? = null,
    val selectedLesson: @RawValue Any? = null,
    val bubbleStops: @RawValue Any? = null,
    val startLocation: String
) : Parcelable
