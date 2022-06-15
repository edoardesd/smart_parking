package com.example.smartparking.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.smartparking.internal.UnitSystem

//const val UNIT_SYSTEM = "UNIT_SYSTEM"
//
//class UnitProviderImpl(context: Context) : UnitProvider {
//    private val appContex = context.applicationContext
//
//    private val preferences: SharedPreferences
//        get() = PreferenceManager.getDefaultSharedPreferences(appContex)
//
//    override fun getUnitSystem(): UnitSystem {
//        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
//        return UnitSystem.valueOf(selectedName!!)
//    }
//}