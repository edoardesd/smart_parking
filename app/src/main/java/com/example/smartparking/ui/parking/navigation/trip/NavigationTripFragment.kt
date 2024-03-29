package com.example.smartparking.ui.parking.navigation.trip

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.TripDetails
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.databinding.NavigationTripFragmentBinding
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.internal.TripDetailsNotFoundException
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_trip_fragment.*


class NavigationTripFragment : ScopedFragment() {

    private lateinit var tripDetailsLocal: TripDetails
    private lateinit var bubbleAdapter: BubbleSelectedAdapter
    private lateinit var binding: NavigationTripFragmentBinding
    private var mapsButton: ImageView? = null

    private lateinit var selectedLesson: LessonListModel
    private var selectedBubbles = ArrayList<BubbleListModel>()
    private val navigationTripViewModel: NavigationTripViewModel by viewModels()

    //    private var navigationText: String? = null
    private var navigationMethod: TransportMode? = null
    private var timeTrip: String? = null
    private var leaveButton: Button? = null
    private var monitorButton: Button? = null
    private lateinit var infoNavigation: InfoText
    private lateinit var startLocation: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.navigation_trip_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationTripViewModel = navigationTripViewModel

        //activate parking
        globalIsParking = true

        val bottomNavigationView: BottomNavigationView =
            activity?.findViewById(R.id.bottom_nav) as BottomNavigationView
        bottomNavigationView.menu.findItem(R.id.navigationChoiceFragment).isChecked = true

        initTripDetails()
        initTitle()
        initDirectionInfo()
        initBubbleSelected()
        initLeaveButton()
        initMonitorButton()
        initImage()
        initMapsButton()

    }

    private fun initMapsButton() {
        mapsButton = view?.findViewById(R.id.iv_maps_preview)
        mapsButton?.setOnClickListener {
            val builder = Uri.Builder()
            builder.scheme("https")
                .authority("www.google.com")
                .path("maps/dir/")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin", tripDetailsLocal.startLocation)
                .appendQueryParameter(
                    "destination",
                    selectedLesson.coordinates
                )
                .appendQueryParameter("travelmode", navigationMethod?.name?.lowercase())
                .appendQueryParameter("arrival_time", infoNavigation.infoTransportTime.startTime?.epoch)
                .appendQueryParameter("traffic_mode", "pessimistic")

            val addressUri = builder.build()

            Log.d(TAG, "My url $addressUri")

            val intent = Intent(Intent.ACTION_VIEW, addressUri)
            intent.setPackage("com.google.android.apps.maps")
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

//            intent.setPackage("com.android.chrome")
            startActivity(intent)
        }
    }

    private fun initImage() {
        var imageName = "${startLocation}_${selectedLesson.parkingPlace.name.lowercase()}_${navigationMethod?.name?.lowercase()}"
        Log.d(TAG, "image name: $imageName")
        context?.let { getDrawable(it, imageName) }?.let { iv_maps_preview.setImageResource(it) }
    }

    private fun getDrawable(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    private fun initTripDetails() {
        val safeArgs = arguments?.let { NavigationTripFragmentArgs.fromBundle(it) }
        if (safeArgs?.tripDetails != null) {
            tripDetailsLocal = safeArgs.tripDetails ?: throw TripDetailsNotFoundException()
            navigationTripViewModel.tripDetails.value = tripDetailsLocal
            arguments?.clear()
        } else {
            tripDetailsLocal = navigationTripViewModel.tripDetails.value!!
        }

        selectedLesson = tripDetailsLocal.selectedLesson as LessonListModel
        infoNavigation = tripDetailsLocal.infoNavigation as InfoText
        startLocation = tripDetailsLocal.startLocation

        navigationMethod = infoNavigation.infoTransportTime.transportMode

        timeTrip = infoNavigation.totalTimeText().toString()
        selectedBubbles = tripDetailsLocal.bubbleStops as ArrayList<BubbleListModel>
    }

    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title =
                "Route to ${selectedLesson.title}"
        }
    }
//    private fun setBoxColor(layout: LinearLayout?, availability: ParkingAvailability) {
//        when(availability){
//            ParkingAvailability.LOW -> layout?.setBackgroundResource(R.drawable.rectangle_gray)
//            ParkingAvailability.MEDIUM -> layout?.setBackgroundResource(R.drawable.rectangle_blue)
//            ParkingAvailability.HIGH -> layout?.setBackgroundResource(R.drawable.rectangle_green)
//        }
//    }


    private fun initDirectionInfo() {
        val stub = view?.findViewById(R.id.layout_stub) as ViewStub
        var infoResource = R.layout.directions_info_car
        var textResource = view?.findViewById<TextView>(R.id.tv_trip_summary)

        tv_arrive_by.text = "Arrive by ${selectedLesson.getTime()}"
        when (navigationMethod) {
            TransportMode.DRIVING -> infoResource = R.layout.directions_info_car
            TransportMode.BICYCLING -> infoResource = R.layout.directions_info_bike
            TransportMode.WALKING -> infoResource = R.layout.directions_info_walk
        }

        setBoxColor(ll_trip_summary, infoNavigation.infoTransportTime.availability)
        stub.layoutResource = infoResource
        stub.inflate()

        var totTimeResource: TextView? = when (navigationMethod) {
            TransportMode.DRIVING -> view?.findViewById(R.id.tv_car_result)
            TransportMode.BICYCLING -> view?.findViewById(R.id.tv_bike_result)
            TransportMode.WALKING -> view?.findViewById(R.id.tv_walk_result)
            else -> null
        }

        var availabilityResource: TextView? = when (navigationMethod) {
            TransportMode.DRIVING -> view?.findViewById(R.id.tv_availability_car)
            TransportMode.BICYCLING -> view?.findViewById(R.id.tv_availability_bike)
            TransportMode.WALKING -> view?.findViewById(R.id.tv_availability_bike)
            null -> view?.findViewById(R.id.tv_availability_car)
        }

        availabilityResource?.text = infoNavigation.infoTransportTime.availability.name
        textResource?.text = Html.fromHtml(infoNavigation.fullText())
        totTimeResource?.text = infoNavigation.totalTimeText()
    }

    private fun initMonitorButton() {
        monitorButton = view?.findViewById(R.id.btn_monitor_parking)
        monitorButton?.setOnClickListener { view ->

            activity?.bottom_nav?.selectedItemId = R.id.controlDisabledFragment
        }
    }


    private fun initLeaveButton() {
        leaveButton = view?.findViewById(R.id.btn_leave)
        leaveButton?.setOnClickListener { view ->
            globalIsParking = false
            Navigation.findNavController(view).navigate(R.id.navigationChoiceFragment)
        }
    }

    private fun initBubbleSelected() {
        selectedBubbles =
            navigationTripViewModel.tripDetails.value!!.bubbleStops as ArrayList<BubbleListModel>
        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }

}