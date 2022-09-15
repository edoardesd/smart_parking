package com.example.smartparking.ui.parking.navigation.trip

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.TripDetails
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.databinding.NavigationTripFragmentBinding
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.internal.TripDetailsNotFoundException
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import kotlinx.android.synthetic.main.directions_info_bike.*
import kotlinx.android.synthetic.main.directions_info_car.*
import kotlinx.android.synthetic.main.directions_info_walk.*
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.android.synthetic.main.navigation_result_fragment.view.*
import kotlinx.android.synthetic.main.navigation_trip_fragment.*


class NavigationTripFragment : ScopedFragment() {

    private lateinit var tripDetailsLocal: TripDetails
    private lateinit var bubbleAdapter: BubbleSelectedAdapter
    private lateinit var binding: NavigationTripFragmentBinding

    private var selectedBubbles = ArrayList<BubbleListModel>()
    private val navigationTripViewModel: NavigationTripViewModel by viewModels()

    private var navigationText: String? = null
    private var navigationMethod: TransportMode? = null
    private var timeTrip: String? = null
    private var leaveButton: Button? = null
    private var monitorButton: Button? = null

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


//        val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav) as BottomNavigationView
//        bottomNavigationView.menu.findItem(R.id.navigationChoiceFragment).isChecked = true




        initTripDetails()

        Log.d(TAG, "nav meth $navigationMethod, nav txt $navigationText")

        initDirectionInfo()
        initBubbleSelected()
        initLeaveButton()
        initMonitorButton()
    }

    private fun initTripDetails() {
        if (arguments != null) {
            val safeArgs = arguments?.let { NavigationTripFragmentArgs.fromBundle(it) }
            tripDetailsLocal = safeArgs?.tripDetails ?: throw TripDetailsNotFoundException()
            Log.d(TAG, "view model not null, getting from safeargs")
            if (navigationTripViewModel.tripDetails.value == null) {
                navigationTripViewModel.tripDetails.value = tripDetailsLocal
                Log.d(TAG, "view model null, storing from safeargs")
            }
        }
        navigationMethod = tripDetailsLocal.navigationMethod
        navigationText = tripDetailsLocal.navigationText
        timeTrip = tripDetailsLocal.totalTimeTrip
        selectedBubbles =  navigationTripViewModel.tripDetails.value!!.bubbleStops as ArrayList<BubbleListModel>
    }

    private fun initDirectionInfo() {
        Log.d(TAG, "in the stub")
        val stub = view?.findViewById(R.id.layout_stub) as ViewStub
        var backgroundResource: Int = R.drawable.rectangle_walk
        var infoResource: Int = R.layout.directions_info_walk
//        var totTimeResource: Int
//        var textResource : Int
        when (navigationMethod) {
            TransportMode.DRIVING -> {
                backgroundResource = R.drawable.rectangle_car
                infoResource = R.layout.directions_info_car
//                tv_car_result.text = tripDetailsLocal.navigationText
//                tv_car_text.text = tripDetailsLocal.totalTimeTrip
            }
            TransportMode.BICYCLING -> {
                backgroundResource = R.drawable.rectangle_bike
                infoResource = R.layout.directions_info_bike


            }
            TransportMode.WALKING -> {
                backgroundResource = R.drawable.rectangle_walk
                infoResource = R.layout.directions_info_walk
//                tv_walk_result.text = tripDetailsLocal.navigationText
            }
        }
        Log.d(TAG, "after the switch")
        ll_trip_summary.setBackgroundResource(backgroundResource)
        stub.layoutResource = infoResource
        stub.inflate()
        val textView = view?.findViewById<TextView>(R.id.tv_trip_summary)

        val timeView = view?.findViewById<TextView>(R.id.tv_bike_result)
//        layout_stub.tv_bike_text.text =  tripDetailsLocal.navigationText
        textView?.text =  tripDetailsLocal.navigationText
        timeView?.text = tripDetailsLocal.totalTimeTrip
    }

    private fun initMonitorButton() {
        monitorButton = view?.findViewById(R.id.btn_monitor_parking)
        monitorButton?.setOnClickListener { view ->
            // TODO pass params

//            val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav) as BottomNavigationView
//            bottomNavigationView.menu.findItem(R.id.controlDisabledFragment).isChecked = true

            Navigation.findNavController(view).navigate(R.id.controlDisabledFragment)
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