package com.example.smartparking.data.recycleList

import android.content.ContentValues
import android.util.Log
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class StudentEngineeringLesson : Users {
    override var listLessons = ArrayList<LessonListModel>()
    private var loadedImg: Int = R.drawable.bonardi_student

    override fun getMyLessons(): ArrayList<LessonListModel> {
        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 19)

        Log.d(ContentValues.TAG, "${datetimeEnd.hours}")

        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)

        listLessons.add(
            LessonListModel("Data bases 2",
            "room e.p.1 | building 32.2".uppercase(),
            "Braga Daniele Maria",
            LessonTime(datetimeStart4, datetimeEnd4),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Software engineering",
            "room 20.s.1 | building 20".uppercase(),
            "Di Nitto Elisabetta",
            LessonTime(datetimeStart4, datetimeEnd4),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Internet of Things",
            "room 20.s.1 | building 20".uppercase(),
            "Cesana Matteo",
            LessonTime(datetimeStart4, datetimeEnd4),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
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