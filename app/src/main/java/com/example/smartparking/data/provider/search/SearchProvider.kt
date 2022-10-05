package com.example.smartparking.data.provider.search

import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.data.MyDate
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import java.util.*

class SearchProvider {
    private var places = ArrayList<LessonListModel>()

    fun getAllPlacesLocal(): ArrayList<LessonListModel>{
        val now = MyDate()
        var datetime = Date(2022, 9, 8)
        datetime.hours = now.hour
        datetime.minutes = now.minutes

        places.add(LessonListModel("Room G.2", "Building 11 - Architettura","Aula didattica | disegno",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Ampere, 2 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room C", "Building 11 - Architettura", "Aula didattica | platea frontale",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Ampere, 2 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room 16B.1.1", "Building 16B", "Aula didattica | disegno",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room B.2.4", "Building 14 - NAVE", "Aula didattica | disegno",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room L.26.16", "Building 26", "Aula didattica | platea frontale",
            LessonTime(datetime, datetime), R.drawable.ponzio_guest,
            parkingPlace = ParkingLots.PONZIO,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Golgi, 20 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room B.5.3", "Building 14 - NAVE", "Aula didattica | disegno",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Bonardi, 9 - 20133 - Milano (MI)"))

        places.add(LessonListModel("Room IIID", "Building 11 - Architettura", "Aula didattica | disegno",
            LessonTime(datetime, datetime), R.drawable.bonardi_guest,
            parkingPlace = ParkingLots.BONARDI,
            coordinates = "45.479743760897044, 9.227082475795326",
            guestDetails = "Via Ampere, 2 - 20133 - Milano (MI)"))

        return places
    }


}