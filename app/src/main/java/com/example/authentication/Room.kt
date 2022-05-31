package com.example.authentication

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.GeoPoint

data class Room(val name: String, val building: Int, val location: GeoPoint?){
    private var latitude = location?.latitude
    private var longitude = location?.longitude

    fun print_me(){
        Log.d(TAG, "Name: $name\n" +
                "Latitude: ${location?.latitude}\n" +
                "Longitude: ${location?.longitude}\n" +
                "Building: ${building}")
    }
}

