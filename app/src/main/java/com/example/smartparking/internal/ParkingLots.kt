package com.example.smartparking.internal

enum class ParkingLots {
    BONARDI, PONZIO
}

val slotsPerParking = mapOf(ParkingLots.BONARDI to 2, ParkingLots.PONZIO to 7)

val freePerParking = mapOf(ParkingLots.BONARDI to 2, ParkingLots.PONZIO to 0)