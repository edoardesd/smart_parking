package com.example.smartparking.data.recycleList

import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.ArrayList

interface Users {
    var listLessons: ArrayList<LessonListModel>

    fun getMyLessons(): ArrayList<LessonListModel>
}
