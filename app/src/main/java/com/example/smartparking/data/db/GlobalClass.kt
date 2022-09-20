package com.example.smartparking.data.db

import android.app.Application

class SmartParkingApplication: Application() {
    companion object {
        var globalIsParking: Boolean = false
        var globalParkingLocation : String? = null
        var globalIsLoggedIn: Boolean = false
    }
}