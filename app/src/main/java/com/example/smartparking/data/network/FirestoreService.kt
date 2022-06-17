package com.example.smartparking.data.network

import android.content.ContentValues
import android.util.Log
import com.example.smartparking.data.db.RoomDetails
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source
import com.google.firebase.inject.Deferred


private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
var roomsArrayList : ArrayList<RoomDetails> = arrayListOf()

interface FirestoreService {

    companion object{

        operator fun invoke(): ArrayList<RoomDetails> {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            firestore.collection("rooms").get(Source.DEFAULT)
                     .addOnCompleteListener { result ->
                        for (document in result.getResult()) {
                            Log.d(ContentValues.TAG, document.toString())
                            val room = RoomDetails(
                                document.data.getValue("room").toString(),
                                document.data.getValue("building").toString().toInt(),
                                document.getGeoPoint("location")!!.latitude,
                                document.getGeoPoint("location")!!.longitude)

                            roomsArrayList.add(room)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }

            return roomsArrayList
        }

    }
}