package com.example.smartparking.data.db

import android.content.ContentValues.TAG
import android.text.Html
import android.text.Spanned
import android.util.Log
import com.example.smartparking.internal.TransportMode
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class InfoText(){
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
        textPark = "park in Via ${infoTransportTime.parkingLot.name.lowercase().capitalize()} "
    }

    private fun setTextAvailable(){
        textAvailable = "(${infoTransportTime.freeSlots}/${infoTransportTime.totalSlots} available) "
    }

    private fun setTextWalk(){
        infoTransportTime.setWalkTime()
        Log.d(TAG, "WALK TIME ${infoTransportTime.walkTime.inWholeMinutes.toInt()}")
        textWalk = "then walk ${infoTransportTime.walkTime.inWholeMinutes.toInt()} minutes. "
    }

    private fun setTextLeave(){
        textLeave = "Leave at ${calculateLeaveTime()?.let { fromEpochToHour(it) }}."
    }

    private fun calculateLeaveTime(): Long? {
        return (infoTransportTime.startTime?.convertToEpoch()
            ?.minus(infoTransportTime.totalTime.inWholeMilliseconds))
    }

    private fun setTextAvgPark(){
        textAvgPark = "\nAverage parking time: ${infoTransportTime.parkingTime.inWholeMinutes.toInt()} minutes."
    }

    private fun fromEpochToHour(epoch: Long): String {
        val stamp = Timestamp(epoch)
        val date = Date(stamp.time)
        val sdf = SimpleDateFormat("HH:mm")

        return sdf.format(date)
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
