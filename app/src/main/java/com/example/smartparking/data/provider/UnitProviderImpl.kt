package com.example.smartparking.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.smartparking.internal.UnitSystem

const val UNIT_SYSTEM_UNIT = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : PreferenceProvider(context), UnitProvider {

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM_UNIT, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}