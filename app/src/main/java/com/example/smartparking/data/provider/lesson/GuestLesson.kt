package com.example.smartparking.data.provider.lesson

import android.content.ContentValues
import android.util.Log
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class GuestLesson : Users {
    override var listLessons = ArrayList<LessonListModel>()
    private var loadedImg: Int = R.drawable.bonardi_guest

    override fun getMyLessons(): ArrayList<LessonListModel> {
        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 19)
        var (datetimeStart5, datetimeEnd5) = getTime(2022, 9, 12, 11, 13)
        var (datetimeStart6, datetimeEnd6) = getTime(2022, 9, 13, 10, 12)

        Log.d(ContentValues.TAG, "${datetimeEnd.hours}")
        listLessons.add(
            LessonListModel("Room IIIA",
            "Building 11 - architettura".uppercase(),
            "Aula didattica | disegno",
            LessonTime(datetimeStart, datetimeEnd),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Ampere, 2 - 20133 - Milano (MI)")
        )

        var (datetimeStart2, datetimeEnd2) = getTime(2022, 9, 11, 9, 19)

        listLessons.add(
            LessonListModel("Room T.0.3",
            "building 13 - trifoglio".uppercase(),
            "Aula didattica | platea frontale",
            LessonTime(datetimeStart2, datetimeEnd2),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
                guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)")
        )

        var (datetimeStart3, datetimeEnd3) = getTime(2022, 9, 12, 9, 19)

        listLessons.add(
            LessonListModel("Room B.2.2",
            "Building 14 - nave".uppercase(),
            "Aula didattica | disegno",
            LessonTime(datetimeStart3, datetimeEnd3),
            loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
                guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)")
        )

        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)

        listLessons.add(
            LessonListModel("Room U.1",
            "building 11 - Architettura".uppercase(),
            "Aula didattica | disegno",
            LessonTime(datetimeStart4, datetimeEnd4),
                loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
                guestDetails = "Via Ampere, 2 - 20133 - Milano (MI)")
        )

        listLessons.add(
            LessonListModel("Room 16B.0.1",
            "building 16b".uppercase(),
            "Aula didattica | disegno",
            LessonTime(datetimeStart5, datetimeEnd5),
                loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
                guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)")
        )

        listLessons.add(
            LessonListModel("Room B.3.4",
            "building 14 - nave".uppercase(),
            "Aula didattica | disegno",
            LessonTime(datetimeStart6, datetimeEnd6),
                loadedImg,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
                guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)")
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