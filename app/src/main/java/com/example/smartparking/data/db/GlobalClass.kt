package com.example.smartparking.data.db

import android.app.Application

class SmartParkingApplication: Application() {
    companion object {
        var globalIsParking: Boolean = false
        var globalIsLoggedIn: Boolean = false

        lateinit var globalDestinationInfo: InfoText
        fun isDestinationInitialized() = ::globalDestinationInfo.isInitialized
    }
}