package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.os.Build
import com.example.smartparking.data.LessonTime

class LessonListModel (val title:String,
                       val description:String,
                       var lessonTime: LessonTime,
                       val preview:Int,
                       var isSelected: Boolean = false) {

    fun lessonToString(): String {
        return "${lessonTime.startDate.day} ${lessonTime.startDate.hours}.${lessonTime.startDate.minutes} - ${lessonTime.endDate.hours}.${lessonTime.endDate.minutes}"
    }
}