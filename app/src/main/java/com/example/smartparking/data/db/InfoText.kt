package com.example.smartparking.data.db

import android.text.Html
import android.text.Spanned
import com.example.smartparking.internal.ParkingAvailability
import com.example.smartparking.internal.TransportMode

class InfoText(val name: String){
    var infoTransportTime = InfoTransportTime()
    private var textRide: String? = null
    private var textPark: String? = null
    private var textAvailable: String? = null
    private var textWalk: String? = null
    private var textLeave: String? = null
    private var textAvgPark: String? = null
    private var textTotal: String? = null

    private fun setTextTotal(){
        infoTransportTime.setTotalTime()
        textTotal = "${infoTransportTime.totalTime.inWholeMinutes.toInt()} min"
    }

    private fun setTextRide(){
        var modeString = "Drive"
        if (infoTransportTime.transportMode == TransportMode.BICYCLING){
            modeString = "Ride"
        }
        textRide = "$modeString ${infoTransportTime.transportTime.inWholeMinutes.toInt()} minutes, "
    }

    private fun setTextPark(){
        textPark = "park in Via ${infoTransportTime.parkingLot} "
    }

    private fun setTextAvailable(){
        textAvailable = "(${infoTransportTime.freeSlots}/${infoTransportTime.totalSlots} available) "
    }

    private fun setTextWalk(){
        textWalk = "then walk ${infoTransportTime.walkTime.inWholeMinutes.toInt()} minutes. "
    }

    private fun setTextLeave(){
        textLeave = "Leave at 10:45."
    }

    private fun setTextAvgPark(){
        textAvgPark = "\nAverage parking time: ${infoTransportTime.parkingTime.inWholeMinutes.toInt()} minutes."
    }

    private fun updateAll(){
        setTextRide()
        setTextPark()
        setTextAvailable()
        setTextWalk()
        setTextLeave()
        setTextAvgPark()
        setTextTotal()
    }

    fun totalTimeText(): Spanned? {
        updateAll()
        return Html.fromHtml(textTotal)
    }

    fun fullText(): String{
        updateAll()
        return textRide +
                textPark +
                textAvailable +
                textWalk +
                textLeave +
                textAvgPark
    }
}
