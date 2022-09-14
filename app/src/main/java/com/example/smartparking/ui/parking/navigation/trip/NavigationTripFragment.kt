package com.example.smartparking.ui.parking.navigation.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.TripDetails
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.internal.TripDetailsNotFoundException
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import kotlinx.android.synthetic.main.navigation_trip_fragment.*


class NavigationTripFragment : Fragment() {

    private lateinit var tripDetails: TripDetails
    private var selectedBubbles = ArrayList<BubbleListModel>()
    private lateinit var viewModel: NavigationTripViewModel
    private lateinit var bubbleAdapter : BubbleSelectedAdapter

    private var leaveButton: Button? = null
    private var monitorButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_trip_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NavigationTripViewModel::class.java)

        val safeArgs = arguments?.let { NavigationTripFragmentArgs.fromBundle(it) }
        tripDetails = safeArgs?.tripDetails ?: throw TripDetailsNotFoundException()
        selectedBubbles = tripDetails.bubbleStops as ArrayList<BubbleListModel>

        initDirectionInfo()
        initBubbleSelected()
        initLeaveButton()
        initMonitorButton()
    }

    private fun initDirectionInfo() {
        val stub = view?.findViewById(R.id.layout_stub) as ViewStub

        ll_trip_summary.setBackgroundResource(R.drawable.rectangle_car)
        stub.layoutResource = R.layout.directions_info_car
        stub.inflate()
    }

    private fun initMonitorButton() {
        monitorButton = view?.findViewById(R.id.btn_monitor_parking)
        monitorButton?.setOnClickListener {view ->
            // TODO pass params
            Navigation.findNavController(view).navigate(R.id.controlFragment)
        }
    }


    private fun initLeaveButton() {
        leaveButton = view?.findViewById(R.id.btn_leave)
        leaveButton?.setOnClickListener {view ->
            globalIsParking = false
            Navigation.findNavController(view).navigate(R.id.navigationChoiceFragment)
        }
    }

    private fun initBubbleSelected() {
        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }

}