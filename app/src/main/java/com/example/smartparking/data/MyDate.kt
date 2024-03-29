package com.example.smartparking.data

import android.content.ContentValues.TAG
import android.util.Log
import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Month
import java.util.*

class MyDate{
    private val calendar : Calendar = Calendar.getInstance()

    var month: Int = calendar.get(Calendar.MONTH)
    var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    var dayOfWeek: String = ""
    var year : Int = calendar.get(Calendar.YEAR)
    var minutes: Int = calendar.get(Calendar.MINUTE)
    var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    var timeMillis: Long = calendar.timeInMillis
    var epoch : String = "Now"

    fun convertToEpoch(): Long {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
        val date = formatter.parse("${day}-${month}-${year} ${hour}:${minutes}") as Date
        return date.time
    }

    fun getEpochAsString(): String{
        epoch = convertToEpoch().toString()
        return epoch
    }

}



