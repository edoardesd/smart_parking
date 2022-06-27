package com.example.smartparking.data.provider

interface LocationProvider {
    suspend fun getPreferredLocation():String
}