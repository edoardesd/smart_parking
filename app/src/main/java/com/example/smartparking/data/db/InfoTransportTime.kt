package com.example.smartparking.data.db

import android.content.ContentValues.TAG
import android.util.Log
import com.example.smartparking.internal.ParkingAvailability
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.internal.slotsPerParking
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
    var freeSlots: Int = 2
    var availability : ParkingAvailability = setAvailability()
    var parkingTime: Duration = setParkTime()

    private fun setAvailability(): ParkingAvailability{
        val parkPercentage: Float = (freeSlots/totalSlots).toFloat()

        if (parkPercentage >= 0.9){
            return ParkingAvailability.HIGH
        }
        if (parkPercentage < 0.90 || parkPercentage >=30){
            return ParkingAvailability.MEDIUM
        }
        return ParkingAvailability.LOW
    }

    private fun setWalkTime(): Duration {
        val time = when (parkingLot) {
            ParkingLots.BONARDI -> 2.minutes
            ParkingLots.PONZIO -> 4.minutes
        }
        return time
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
                TransportMode.DRIVING -> (2..5).random()
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

