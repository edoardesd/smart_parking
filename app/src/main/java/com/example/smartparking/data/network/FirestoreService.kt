//package com.example.smartparking.data.network
//
//import android.content.ContentValues
//import android.util.Log
//import com.example.smartparking.data.db.FirestormRoom
//import com.example.smartparking.data.db.RoomDetails
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.QuerySnapshot
//import com.google.firebase.firestore.Source
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.google.gson.annotations.SerializedName
//import kotlinx.coroutines.Deferred
//import okhttp3.Interceptor
//
//private var db = Firebase.firestore
//var roomsArrayList : ArrayList<String> = arrayListOf<String>()
//var roomsDetailsArrayList : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()
//
//interface FirestoreService {
//
//    fun getDatabaseFirestore(
//        building: Int,
//        location: String,
//        plusCode: String,
//        room: String
//    ): Deferred<FirestormRoom>
//
//    companion object{
//        operator fun invoke(
//            connectivityInterceptor: ConnectivityInterceptor
//        ): Task<QuerySnapshot> {
//            val callInterceptor = Interceptor {chain ->
//                val db.collection("rooms").get(Source.DEFAULT)
//                    .addOnCompleteListener { result ->
//                Log.d(ContentValues.TAG, result.getResult().toString())
//                for (document in result.getResult()) {
//                    Log.d(ContentValues.TAG, document.toString())
//
//                    val room = RoomDetails(
//                        document.data.getValue("room").toString(),
//                        document.data.getValue("building").toString().toInt(),
//                        document.getGeoPoint("location")!!.latitude,
//                        document.getGeoPoint("location")!!.longitude)
//
//                    roomsDetailsArrayList.add(room)
//                    roomsArrayList.add(room.name)
//                }
//                Log.d(ContentValues.TAG, "Rooms inside: $roomsArrayList")
//                Log.d(ContentValues.TAG, "rooms ${roomsDetailsArrayList.map { x -> x.name }}")
//
//            }
//                .addOnFailureListener { exception ->
//                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
//                }
//            }
//
//            return
//        }
//    }
//}