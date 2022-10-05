package com.example.smartparking.data.provider.lesson

import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class StudentEngineeringLesson : Users {
    override var listLessons = ArrayList<LessonListModel>()

    override fun getMyLessons(): ArrayList<LessonListModel> {
        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 11)
        var (datetimeStart2, datetimeEnd2) = getTime(2022, 9, 11, 8, 10)
        var (datetimeStart3, datetimeEnd3) = getTime(2022, 9, 11, 14, 16)
        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)
        var (datetimeStart5, datetimeEnd5) = getTime(2022, 9, 12, 11, 13)
        var (datetimeStart6, datetimeEnd6) = getTime(2022, 9, 13, 10, 12)
        var (datetimeStart7, datetimeEnd7) = getTime(2022, 9, 14, 13, 16)


        listLessons.add(
            LessonListModel("Data bases 2",
            "room e.p.1 | building 32.2".uppercase(),
            "Prof. Braga Daniele Maria",
            LessonTime(datetimeStart, datetimeEnd),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Software engineering",
            "room 20.s.1 | building 20".uppercase(),
            "Prof. Di Nitto Elisabetta",
            LessonTime(datetimeStart2, datetimeEnd2),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Computing infrastructures",
                "room 9.0.1 | building 9".uppercase(),
                "Prof. Cremonesi Paolo",
                LessonTime(datetimeStart3, datetimeEnd3),
                R.drawable.ponzio_student,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Internet of Things",
            "room 20.s.1 | building 20".uppercase(),
            "Prof. Cesana Matteo",
            LessonTime(datetimeStart4, datetimeEnd4),
            R.drawable.ponzio_student,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Production System",
                "room 21.s.1 | building 21".uppercase(),
                "Prof. Ferrarini Luca",
                LessonTime(datetimeStart5, datetimeEnd5),
                R.drawable.ponzio_student,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Cellular Bioengineering",
                "room 3.1.3 | building 3".uppercase(),
                "Prof. Soncini Monica",
                LessonTime(datetimeStart6, datetimeEnd6),
                R.drawable.ponzio_student,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Complex systems dynamics",
                "room 25.s.1 | building 25".uppercase(),
                "Prof. Piccardi Carlo",
                LessonTime(datetimeStart7, datetimeEnd7),
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