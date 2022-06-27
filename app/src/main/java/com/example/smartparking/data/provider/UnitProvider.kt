package com.example.smartparking.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.smartparking.internal.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"


interface UnitProvider {
     fun getUnitSystem(): UnitSystem
}