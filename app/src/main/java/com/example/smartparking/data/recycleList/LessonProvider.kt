package com.example.smartparking.data.recycleList

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalUserType
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.internal.UserType
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class LessonProvider {

    private var lesson = ArrayList<LessonListModel>()
    private var loadedImg: Int = R.drawable.bonardi_student
    private var studentArchitectureLesson = StudentArchitectureLesson()
    private var studentEngineeringLesson = StudentEngineeringLesson()

    fun getAllLessonsLocal(): ArrayList<LessonListModel> {
        return when (globalUserType) {
            UserType.STUDENT_ARCHITECTURE -> studentArchitectureLesson.getMyLessons()
            UserType.STUDENT_ENGINEERING -> studentEngineeringLesson.getMyLessons()
            UserType.PROF_ARCHITECTURE -> TODO()
            UserType.PROF_ENGINEERING -> TODO()
            UserType.GUEST -> TODO()
        }
    }
//        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 19)
//
//        Log.d(TAG, "${datetimeEnd.hours}")
//        listLessons.add(LessonListModel("Architectural design studio",
//            "room iiiC | building 11".uppercase(),
//            "Zucchi Cino Paolo",
//            LessonTime(datetimeStart, datetimeEnd),
//            loadedImg,
//        parkingPlace = ParkingLots.BONARDI,
//        coordinates = "45.479743760897044, 9.227082475795326"))
//
//        var (datetimeStart2, datetimeEnd2) = getTime(2022, 9, 11, 9, 19)
//
//        listLessons.add(LessonListModel("Space and society",
//            "room 3.0.1 | building 3".uppercase(),
//            "Costa Giuliana",
//            LessonTime(datetimeStart2, datetimeEnd2),
//            loadedImg,
//            parkingPlace = ParkingLots.BONARDI,
//            coordinates = "45.479743760897044, 9.227082475795326"))
//
//        var (datetimeStart3, datetimeEnd3) = getTime(2022, 9, 12, 9, 19)
//
//        listLessons.add(LessonListModel("Thematic studio",
//            "room b.6.3 | building 14".uppercase(),
//            "Fernandez Elorza Hector Daniel",
//            LessonTime(datetimeStart3, datetimeEnd3),
//            loadedImg,
//            parkingPlace = ParkingLots.BONARDI,
//            coordinates = "45.479743760897044, 9.227082475795326"))
//
//        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)
//
//        listLessons.add(LessonListModel("Data bases 2",
//            "room e.p.1 | building 32.2".uppercase(),
//            "Braga Daniele Maria",
//            LessonTime(datetimeStart4, datetimeEnd4),
//            R.drawable.ponzio_student,
//            parkingPlace = ParkingLots.PONZIO,
//            coordinates = "45.479743760897044, 9.227082475795326"))
//
//        listLessons.add(LessonListModel("Software engineering",
//            "room 20.s.1 | building 20".uppercase(),
//            "Di Nitto Elisabetta",
//            LessonTime(datetimeStart2, datetimeEnd2),
//            R.drawable.ponzio_student,
//            parkingPlace = ParkingLots.PONZIO,
//            coordinates = "45.479743760897044, 9.227082475795326"))
//
//        listLessons.add(LessonListModel("Internet of Things",
//            "room 20.s.1 | building 20".uppercase(),
//            "Cesana Matteo",
//            LessonTime(datetimeStart2, datetimeEnd2),
//            R.drawable.ponzio_student,
//            parkingPlace = ParkingLots.PONZIO,
//            coordinates = "45.479743760897044, 9.227082475795326"))
//
//        return listLessons
//    }
//
//    private fun getTime (year: Int, month: Int, day: Int, hourStart: Int, hourEnd: Int): Pair<Date, Date> {
//        var start = Date(year, month, day)
//        var end = Date(year, month, day)
//
//        start.hours = hourStart
//        start.minutes = 15
//        end.hours = hourEnd
//        end.minutes = 15
//
//        return start to end
//    }
}