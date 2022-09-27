package com.example.smartparking.data.recycleList

import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class LessonProvider {

    private var listLessons = ArrayList<LessonListModel>()

    fun getAllLessonsLocal(): ArrayList<LessonListModel>{

        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 19)

        listLessons.add(LessonListModel("Architectural design studio",
            "room iiiC | building 11".uppercase(),
            "Zucchi Cino Paolo",
            LessonTime(datetimeStart, datetimeEnd),
            R.drawable.bonardi_student_1,
        parkingPlace = "Via Bonardi",
        coordinates = "45.479743760897044, 9.227082475795326"))
        
        listLessons.add(LessonListModel("Space and society",
            "room 3.0.1 | building 3".uppercase(),
            "Costa Giuliana",
            LessonTime(datetimeStart, datetimeEnd),
            R.drawable.bonardi_student_1,
            parkingPlace = "Via Bonardi",
            coordinates = "45.479743760897044, 9.227082475795326"))

        listLessons.add(LessonListModel("Thematic studio",
            "room b.6.3 | building 14".uppercase(),
            "Fernandez Elorza Hector Daniel",
            LessonTime(datetimeStart, datetimeEnd),
            R.drawable.bonardi_student_1,
            parkingPlace = "Via Bonardi",
            coordinates = "45.479743760897044, 9.227082475795326"))



        return listLessons

    }

    private fun getTime (year: Int, month: Int, day: Int, hourStart: Int, hourEnd: Int): Pair<Date, Date> {

        var start = Date(year, month, day)
        var end = Date(year, month, day)

        start.hours = hourStart
        start.minutes = 15
        start.hours = hourEnd
        start.minutes = 15

        return start to end
    }


}