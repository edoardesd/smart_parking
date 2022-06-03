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
import com.example.smartparking.data.RoomDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.navigation_choice_fragment.*
import java.util.*


class NavigationChoiceFragment : Fragment() {

    private var db = Firebase.firestore
    lateinit var bindingView: View

    private var timeButton: Button? = null
    private var goButton: Button? = null
    private var date = MyDate()
    private var item_index : Int = 0
//    private var locations : List<RoomDetails>

//    private var hour : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//    private var minute : Int = Calendar.getInstance().get(Calendar.MINUTE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingView = inflater.inflate(R.layout.navigation_choice_fragment,container,false)
        return bindingView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTextView(getAllRooms(db))
        initTimePicker()
        initGoButton()
    }

    private fun initGoButton() {
        goButton = view?.findViewById(R.id.btn_go)
        goButton?.setOnClickListener {view ->
            showNavigationDetails(view)
        }
    }

    private fun showNavigationDetails(view: View) {
        Log.d(TAG, timeButton?.text.toString().lowercase())
        var room = RoomDetails("2.1.1", 2, 45.47811143966341,9.23460752183241)
        val navigationDetail = NavigationDetails(room, date.epoch)
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

    private fun initTextView(locations: ArrayList<RoomDetails>) {
        val adapter = ArrayAdapter(
            requireView().context,
            R.layout.list_item,
            locations
//            getLocations()
//            getAllRooms(db)
        )
        tv_auto_complete_text_view.setAdapter(adapter)
    }

    private fun getLocations() : Array<out String> {
        return resources.getStringArray(R.array.campus_location)
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

    private fun getAllRooms(database : FirebaseFirestore): ArrayList<RoomDetails> {
        var roomsArrayList : ArrayList<RoomDetails> = arrayListOf<RoomDetails>()

        database.collection("rooms").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.data.getValue("room").toString()
                    Log.d(TAG, name)

                    val building = document.data.getValue("building").toString().toInt()
                    val location = document.getGeoPoint("location")
                    val room = RoomDetails(name, building, location!!.latitude, location!!.longitude)

//                    room.print_me()
                    roomsArrayList.add(room)
                }
                Log.d(TAG, "Rooms: $roomsArrayList")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return roomsArrayList
    }
}
