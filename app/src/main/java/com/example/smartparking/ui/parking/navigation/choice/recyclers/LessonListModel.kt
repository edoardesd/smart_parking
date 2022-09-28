package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.os.Build
import com.example.smartparking.data.LessonTime
import com.example.smartparking.internal.ParkingLots

class LessonListModel (val title:String,
                       val description:String,
                       var prof: String,
                       var lessonTime: LessonTime,
                       val preview:Int,
                       val parkingPlace: ParkingLots = ParkingLots.PONZIO,
                       val coordinates: String = "",
                       var isSelected: Boolean = false) {

    fun lessonToString(): String {
        return "${lessonTime.startDate.day} ${lessonTime.startDate.hours}.${lessonTime.startDate.minutes} - ${lessonTime.endDate.hours}.${lessonTime.endDate.minutes}"
    }
}