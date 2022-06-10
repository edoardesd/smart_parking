//package com.example.smartparking.data.db
//
//
//import android.app.usage.UsageEvents
//import android.media.metrics.Event
//
//
//
//data class FirestormRoom(
//    val building: String = "",
//    val location: String = "",
//    val plusCode: String? = "",
//    var room: String = ""
//){
//    private var _events: ArrayList<Event> = ArrayList<Event>()
//
//    var events : ArrayList<Event> = _events
//        set(value) {_events = value}
//
//    override fun toString(): String {
//        return "$building $location $plusCode $location"
//    }
//}
//
////    ) : Parcelable {
////
////    companion object {
////        fun DocumentSnapshot.toRoom(): FirestormRoom? {
////            try {
////                val building = getString("building")!!
////                val location = getString("profile_image")!!
////                val plusCode = getString("user_bio")
////                val room = getString("room")!!
////                return FirestormRoom(building, location, plusCode, room)
////            } catch (e: Exception) {
////                Log.e(TAG, "Error converting fields", e)
////                FirebaseCrashlytics.getInstance().log("Error converting user profile")
////                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
////                FirebaseCrashlytics.getInstance().recordException(e)
////                return null
////            }
////        }
////        private const val TAG = "Firebase to room"
////    }
////}
////
////object FirebaseProfileService {
////    private const val TAG = "FirebaseProfileService"
////    suspend fun getProfileData(building: String, location: String, plusCode: String?, room: String): FirestormRoom? {
////        val db = FirebaseFirestore.getInstance()
////        return try {
////            db.collection("users").document("Room 5.0.1").get().await().toRoom()
////        } catch (e: Exception) {
////            Log.e(TAG, "Error getting user details", e)
////            FirebaseCrashlytics.getInstance().log("Error getting user details")
////            FirebaseCrashlytics.getInstance().recordException(e)
////            null
////        }
////    }
////}