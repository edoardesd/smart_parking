package com.example.smartparking.data.db

import android.content.ContentValues.TAG
import android.util.Log
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreeBonardi
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalParkingFreePonzio
import com.example.smartparking.internal.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class InfoTransportTime {
    var transportMode: TransportMode = TransportMode.DRIVING
    var parkingLot: ParkingLots = ParkingLots.BONARDI
    var transportTime: Duration = 12.minutes
    var totalTime: Duration = 0.seconds
    var walkTime: Duration = setWalkTime()
    var totalSlots: Int = slotsPerParking[parkingLot]!!
    var freeSlots: Int = freePerParking[parkingLot]!!
    var availability : ParkingAvailability = setAvailability()
    var parkingTime: Duration = setParkTime()
    var startTime: MyDate? = null

    private fun updateSlots(){
        totalSlots = slotsPerParking[parkingLot]!!
        freeSlots = when(parkingLot){
            ParkingLots.BONARDI -> globalParkingFreeBonardi
            ParkingLots.PONZIO -> globalParkingFreePonzio
        }
    }

    fun setAvailability(): ParkingAvailability{
        updateSlots()
        var parkPercentage = 1.0F
        if (transportMode == TransportMode.DRIVING) {
            parkPercentage = (freeSlots / totalSlots).toFloat()
            if(parkingLot == ParkingLots.BONARDI) {return ParkingAvailability.MEDIUM}
        }
        else{
            totalSlots = 30
            freeSlots = totalSlots - 3
        }

        Log.d(TAG, "free, total percentage ${freeSlots} $totalSlots $parkPercentage")
        if (parkPercentage >= 0.9){
            return ParkingAvailability.HIGH
        }
        if (parkPercentage < 0.90 && parkPercentage >=0.3){
            return ParkingAvailability.MEDIUM
        }
        return ParkingAvailability.LOW
    }

    fun setWalkTime(): Duration {
        val time = when (parkingLot) {
            ParkingLots.BONARDI -> getBonardiTime()
            ParkingLots.PONZIO -> getPonzioTime()
        }
        Log.d(TAG, "walk time $time")
        walkTime = time
        return time
    }

    private fun getBonardiTime(): Duration {
        Log.d(TAG, "get transport mode ${transportMode}")
        return when (transportMode) {
            TransportMode.DRIVING -> DEFAULT_CAR_WALK_BONARDI_TIME
            TransportMode.BICYCLING -> DEFAULT_BIKE_WALKING_TIME
            TransportMode.WALKING -> 1.minutes
        }
    }

    private fun getPonzioTime(): Duration {
        return when (transportMode) {
            TransportMode.DRIVING -> DEFAULT_CAR_WALK_PONZIO_TIME
            TransportMode.BICYCLING -> DEFAULT_BIKE_WALKING_TIME
            TransportMode.WALKING -> 1.minutes
        }
    }

    fun setParkTime(): Duration {
        Log.d(TAG, "Transport mode $transportMode")
        val time = when(availability){
            ParkingAvailability.LOW -> {when(transportMode){
                TransportMode.DRIVING -> (5..15).random()
                TransportMode.BICYCLING -> (2..5).random()
                TransportMode.WALKING -> 0
            }
            }
            ParkingAvailability.MEDIUM -> {
                when (transportMode) {
                    TransportMode.DRIVING -> (3..10).random()
                    TransportMode.BICYCLING -> (1..3).random()
                    TransportMode.WALKING -> 0
                }
            }
            ParkingAvailability.HIGH ->  when (transportMode) {
                TransportMode.DRIVING -> (1..3).random()
                TransportMode.BICYCLING -> (1..2).random()
                TransportMode.WALKING -> 0
            }
        }
        parkingTime = time.minutes
        return time.minutes

    }

    fun setTotalTime(){
        totalTime = transportTime + parkingTime + walkTime
    }
}

