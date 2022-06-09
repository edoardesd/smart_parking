package com.example.smartparking.ui.parking.navigation.choice

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.smartparking.R
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.provider.UnitProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.navigation_choice_fragment.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList



class NavigationChoiceFragment : Fragment() {

    private var db = Firebase.firestore
    private var roomsCollectionRef = Firebase.firestore.collection("rooms")
//    private var my_locations : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()
    var roomsArrayList : ArrayList<String> = arrayListOf<String>()
    var roomsDetailsArrayList : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()

    var selectedLocation : RoomDetails? = null

//    val roomsRef = db.collection("rooms")

    lateinit var bindingView: View

    private var timeButton: Button? = null
    private var goButton: Button? = null
    private var date = MyDate()
    private var item_index : Int = 0

//    private lateinit var locations : List<RoomDetails>

//    private var hour : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//    private var minute : Int = Calendar.getInstance().get(Calendar.MINUTE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        bindingView = inflater.inflate(R.layout.navigation_choice_fragment,container,false)
        return bindingView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val locations : List<RoomDetails>


//        readData { roomsList -> Log.d(TAG, "$roomsList") }
//        retrieveRooms()
        getAllRooms(db)
        initTimePicker()
        initGoButton()
//        initTextView(getAllRooms(db))
        initTextView()

    }

    private fun initGoButton() {
        goButton = view?.findViewById(R.id.btn_go)
        goButton?.setOnClickListener {view ->
            showNavigationDetails(view)
        }
    }
    private fun retrieveRooms() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = roomsCollectionRef.get().addOnCompleteListener { result ->
                for (document in result.getResult()) {
                    val room = RoomDetails(
                        document.data.getValue("room").toString(),
                        document.data.getValue("building").toString().toInt(),
                        document.getGeoPoint("location")!!.latitude,
                        document.getGeoPoint("location")!!.longitude)

                    Log.d(TAG, "log query snapshot ${document.data.getValue("room").toString()}")
                    Log.d(TAG, room.toString())

                    roomsArrayList.add(room.name)
                    roomsDetailsArrayList.add(room)

                }
//                Log.d(TAG, "Rooms inside: $roomsArrayList")
//                Log.d(TAG, "rooms ${roomsArrayList.map { x -> x.name }}")

            }
//            for (document in querySnapshot) {
//                val myRoom = document.toObject<RoomDetails>()
//                Log.d(TAG, myRoom)
//            }

//            withContext(Dispatchers.Main) {
//                Log.d(TAG, "context")
//            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d(TAG, "error")
            }
        }
    }
    private fun showNavigationDetails(view: View) {
        Log.d(TAG, timeButton?.text.toString().lowercase())
        val navigationDetail = NavigationDetails(selectedLocation!!, date.epoch)
        val actionDetail = NavigationChoiceFragmentDirections.actionResult(navigationDetail)

        with(Navigation) {
            findNavController(view).navigate(actionDetail)
        }
    }

    private fun initTimePicker() {
        timeButton = view?.findViewById(R.id.btn_time)
        timeButton?.text = String.format("now")
        timeButton?.setOnClickListener{
            openTimePicker()
        }
    }

    private fun initTextView() {
        val adapter = ArrayAdapter(
            requireView().context,
            R.layout.list_item,
            roomsArrayList
        )

        tv_auto_complete_text_view.setAdapter(adapter)
        tv_auto_complete_text_view.setOnItemClickListener { adapterView, view, position, id ->
//            selectedLocation = adapterView.getItemAtPosition(position) as RoomDetails
            Log.d(TAG, "selected this item: ${  position}")
            selectedLocation = roomsDetailsArrayList[position]
        }
    }

    private fun openTimePicker(){
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _timePicker, selectedHour, selectedMinute ->
                date.minutes = selectedMinute
                date.hour = selectedHour
                updateText(selectedHour, selectedMinute)
                Log.d(TAG, "epoch: ${date.getEpochAsString()}")
            }

        val timePickerDialog =
            TimePickerDialog(requireView().context,
                AlertDialog.THEME_HOLO_DARK,  // set theme
                onTimeSetListener,
                date.hour,
                date.minutes,
                true)
        timePickerDialog.setTitle("Select Departure Time")
        timePickerDialog.show()

    }

    private fun updateText(_hour: Int, _minute: Int) {
        Log.d(TAG, "Hour $_hour, Minute $_minute")
        timeButton?.text = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            _hour,
            _minute,
        )
    }

    fun getAllRooms(database : FirebaseFirestore) {
//        var roomsArrayList : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()
        database.collection("rooms").get(Source.DEFAULT)
            .addOnCompleteListener { result ->
                for (document in result.getResult()) {
                    val room = RoomDetails(
                        document.data.getValue("room").toString(),
                        document.data.getValue("building").toString().toInt(),
                        document.getGeoPoint("location")!!.latitude,
                        document.getGeoPoint("location")!!.longitude)

                    roomsDetailsArrayList.add(room)
                    roomsArrayList.add(room.name)
                }
                Log.d(TAG, "Rooms inside: $roomsArrayList")
                Log.d(TAG, "rooms ${roomsDetailsArrayList.map { x -> x.name }}")

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }

//    fun interface FirestoreCallback {
//        fun onCallback(roomList: ArrayList<RoomDetails?>?)
//    }

//    fun readData(firestoreCallback: FirestoreCallback) {
//        db.collection("rooms").get(Source.DEFAULT)
//            .addOnCompleteListener { result ->
//                for (document in result.getResult()) {
//                    val room = RoomDetails(
//                        document.data.getValue("room").toString(),
//                        document.data.getValue("building").toString().toInt(),
//                        document.getGeoPoint("location")!!.latitude,
//                        document.getGeoPoint("location")!!.longitude)
//
//                    my_locations.add(room)
//                }
//                firestoreCallback(my_locations)
//                Log.d(TAG, "Rooms: $my_locations")
////                Log.d(TAG, "rooms ${roomsArrayList.map { x -> x.name }}")
//
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
//    }


}
