package com.example.smartparking.data.provider.lesson

import android.content.ContentValues
import android.util.Log
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class StudentArchitectureLesson : Users {
    override var listLessons = ArrayList<LessonListModel>()
    private var loadedImg: Int = R.drawable.bonardi_student

    override fun getMyLessons(): ArrayList<LessonListModel> {
        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 19)
        var (datetimeStart5, datetimeEnd5) = getTime(2022, 9, 12, 11, 13)
        var (datetimeStart6, datetimeEnd6) = getTime(2022, 9, 13, 10, 12)

        Log.d(ContentValues.TAG, "${datetimeEnd.hours}")
        listLessons.add(
            LessonListModel("Architectural design studio",
            "room iiiC | building 11".uppercase(),
            "Zucchi Cino Paolo",
            LessonTime(datetimeStart, datetimeEnd),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        var (datetimeStart2, datetimeEnd2) = getTime(2022, 9, 11, 9, 19)

        listLessons.add(
            LessonListModel("Space and society",
            "room 3.0.1 | building 3".uppercase(),
            "Costa Giuliana",
            LessonTime(datetimeStart2, datetimeEnd2),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        var (datetimeStart3, datetimeEnd3) = getTime(2022, 9, 12, 9, 19)

        listLessons.add(
            LessonListModel("Thematic studio",
            "room b.6.3 | building 14".uppercase(),
            "Fernandez Elorza Hector Daniel",
            LessonTime(datetimeStart3, datetimeEnd3),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)

        listLessons.add(
            LessonListModel("History and theory",
            "room b.4.3 | building 14".uppercase(),
            "Irace Fulvio",
            LessonTime(datetimeStart4, datetimeEnd4),
            R.drawable.bonardi_student,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Structural design",
            "room 9.1.2 | building 9".uppercase(),
            "Di Valente Marco Vincenzo",
            LessonTime(datetimeStart5, datetimeEnd5),
            R.drawable.bonardi_student,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Project appraisal",
            "room B | building 1".uppercase(),
            "Pandolfi alessandra Maria",
            LessonTime(datetimeStart6, datetimeEnd6),
            R.drawable.bonardi_student,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        return listLessons
    }

    private fun getTime (year: Int, month: Int, day: Int, hourStart: Int, hourEnd: Int): Pair<Date, Date> {
        var start = Date(year, month, day)
        var end = Date(year, month, day)

        start.hours = hourStart
        start.minutes = 15
        end.hours = hourEnd
        end.minutes = 15

        return start to end
    }
}