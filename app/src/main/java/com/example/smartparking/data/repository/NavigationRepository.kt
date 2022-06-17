package com.example.smartparking.data.repository

import androidx.lifecycle.LiveData
import com.example.smartparking.data.network.response.GoogleDirectionResponse

interface NavigationRepository {
    suspend fun getNavigation(origins: String,
                              destinations: String,
                              sensor: String,
                              units: String,
                              mode: String): LiveData<GoogleDirectionResponse>
}