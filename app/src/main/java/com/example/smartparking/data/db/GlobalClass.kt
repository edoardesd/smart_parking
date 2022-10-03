package com.example.smartparking.data.db

import android.app.Application
import com.example.smartparking.internal.UserType

class SmartParkingApplication: Application() {
    companion object {
        var globalIsParking: Boolean = false
        var globalIsLoggedIn: Boolean = false
        var globalUserType: UserType = UserType.STUDENT_ARCHITECTURE

        lateinit var globalDestinationInfo: InfoText
        fun isDestinationInitialized() = ::globalDestinationInfo.isInitialized
    }
}