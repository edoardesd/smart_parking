package com.example.smartparking.data.provider

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.smartparking.internal.LocationPermissionNotGrantedException
import com.example.smartparking.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.jar.Manifest

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun getPreferredLocation(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }
        else {
            return "${getCustomLocationName()}"
        }
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastDeviceLocation(): Location? {
        return (if (hasLocationPermissionFine())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()).await()
    }

//    private fun hasLocationPermissionCoarse(): Boolean {
//        return ContextCompat.checkSelfPermission(appContext,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//    }

    private fun hasLocationPermissionFine(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}