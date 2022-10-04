package com.example.smartparking.internal

import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreeBonardi
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreePonzio

enum class ParkingLots {
    BONARDI, PONZIO
}

val slotsPerParking = mapOf(ParkingLots.BONARDI to 2, ParkingLots.PONZIO to 7)

val freePerParking = mapOf(ParkingLots.BONARDI to globalParkingFreeBonardi, ParkingLots.PONZIO to globalParkingFreePonzio)