package com.example.smartparking.ui.parking.navigation.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.TripDetails
import com.example.smartparking.internal.TripDetailsNotFoundException
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.android.synthetic.main.navigation_trip_fragment.*


class NavigationTripFragment : Fragment() {

//    companion object {
//        fun newInstance() = NavigationTripFragment()
//    }

    private lateinit var tripDetails: TripDetails
    private var selectedBubbles = ArrayList<BubbleListModel>()
    private lateinit var viewModel: NavigationTripViewModel
    private lateinit var bubbleAdapter : BubbleSelectedAdapter


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

        ll_trip_summary.setBackgroundResource(R.drawable.rectangle_car)

        val stub = view?.findViewById(R.id.layout_stub) as ViewStub
        stub.layoutResource = R.layout.directions_info_car

        stub.inflate()

        initBubbleSelected()
    }

    private fun initBubbleSelected() {
        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }

}