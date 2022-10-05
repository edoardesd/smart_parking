package com.example.smartparking.data.provider.lesson

import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class ProfEngineeringLesson : Users {
    override var listLessons = ArrayList<LessonListModel>()
    private var loadedImg: Int = R.drawable.ponzio_prof

    override fun getMyLessons(): ArrayList<LessonListModel> {
        var (datetimeStart, datetimeEnd) = getTime(2022, 9, 10, 9, 11)
        var (datetimeStart2, datetimeEnd2) = getTime(2022, 9, 11, 8, 10)
        var (datetimeStart3, datetimeEnd3) = getTime(2022, 9, 11, 14, 16)
        var (datetimeStart4, datetimeEnd4) = getTime(2022, 9, 10, 14, 16)
        var (datetimeStart5, datetimeEnd5) = getTime(2022, 9, 12, 11, 13)
        var (datetimeStart6, datetimeEnd6) = getTime(2022, 9, 13, 10, 12)
        var (datetimeStart7, datetimeEnd7) = getTime(2022, 9, 14, 13, 16)


        listLessons.add(
            LessonListModel("Fondamenti di internet e reti",
            "room 5.0.3 | building 5".uppercase(),
            "Aula didattica | platea frontale",
            LessonTime(datetimeStart, datetimeEnd),
                loadedImg,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Your office",
            "building 20".uppercase(),
            "DEIB",
            LessonTime(datetimeStart2, datetimeEnd2),
            loadedImg,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Research laboratory",
                "room T.0.3 | building 13".uppercase(),
                "Aula didattica | platea frontale",
                LessonTime(datetimeStart3, datetimeEnd3),
                loadedImg,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Internet of Things",
            "room 20.s.1 | building 20".uppercase(),
            "Aula didattica | platea frontale",
            LessonTime(datetimeStart4, datetimeEnd4),
                loadedImg,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("ANTLAB",
                "building 20".uppercase(),
                "Laboratory",
                LessonTime(datetimeStart5, datetimeEnd5),
                loadedImg,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Cellular Bioengineering",
                "room 3.1.3 | building 3".uppercase(),
                "Prof. Soncini Monica",
                LessonTime(datetimeStart6, datetimeEnd6),
                loadedImg,
                parkingPlace = ParkingLots.PONZIO,
                coordinates = "45.479743760897044, 9.227082475795326")
        )

        listLessons.add(
            LessonListModel("Complex systems dynamics",
                "room 25.s.1 | building 25".uppercase(),
                "Prof. Piccardi Carlo",
                LessonTime(datetimeStart7, datetimeEnd7),
                loadedImg,
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