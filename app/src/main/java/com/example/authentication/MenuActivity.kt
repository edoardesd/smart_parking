package com.example.authentication

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.String
import java.util.*

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var roomsArrayList : ArrayList<Room>

    private val db = Firebase.firestore

    private var timeButton: Button? = null
    private var hour : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private var minute : Int = Calendar.getInstance().get(Calendar.MINUTE)
    private lateinit var room_selection : kotlin.String
    private var item_index : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timeButton = findViewById(R.id.btn_time)
        timeButton?.text = String.format("NOW")

//        authenticate on firebase
//        auth = Firebase.auth
        Firebase.database.setPersistenceEnabled(true)
        val db_reference = Firebase.database.getReference("rooms")
        db_reference.keepSynced(true)
        Log.d(TAG, "persistence $db_reference")
        val firestore = FirebaseFirestore.getInstance()
//        firestore.useEmulator("10.0.2.2", 8080)

        val locations = getAllRooms(db)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            locations
        )

        with(binding.tvAutoCompleteTextView) {
            setAdapter(adapter)
//            onItemClickListener = this@MenuActivity
        }

        // set intent for the button and pass parameters
        val button:Button = findViewById(R.id.btn_go)
        button.setOnClickListener {
            Log.d(TAG, locations[item_index].toString())
            Log.d(TAG, timeButton?.text.toString().lowercase())
            val intent = Intent(this@MenuActivity, ResultActivity::class.java)
            intent.putExtra("room", locations[item_index].name)
            intent.putExtra("latitude", locations[item_index].location?.latitude)
            intent.putExtra("longitude", locations[item_index].location?.longitude)
            intent.putExtra("hour", hour)
            intent.putExtra("minute", minute)
            startActivity(intent)
        }

    }

    fun popTimePicker(view: View?) {
        val onTimeSetListener =
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                Log.d(TAG, "Hour $hour, Minute $minute")
                var am_pm = ""
                // AM_PM decider logic
                when {hour == 0 -> { hour += 12
                    am_pm = "AM"
                }
                    hour == 12 -> am_pm = "PM"
                    hour > 12 -> { hour -= 12
                        am_pm = "PM"
                    }
                    else -> am_pm = "AM"
                }
                timeButton?.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, am_pm))
            }

        val style: Int = AlertDialog.THEME_HOLO_DARK
        val timePickerDialog =
            TimePickerDialog(this,  style, onTimeSetListener, hour, minute, false)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }
}

private fun getAllRooms(database : FirebaseFirestore): ArrayList<Room> {
    var roomsArrayList : ArrayList<Room> = arrayListOf<Room>()

    database.collection("rooms").get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val name = document.data.getValue("room").toString()
                val building = document.data.getValue("building").toString().toInt()
                val location = document.getGeoPoint("location")
                val room = Room(name, building, location)

                room.print_me()
                roomsArrayList.add(room)
            }
            Log.d(TAG, "Rooms: $roomsArrayList")
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }

    return roomsArrayList
}