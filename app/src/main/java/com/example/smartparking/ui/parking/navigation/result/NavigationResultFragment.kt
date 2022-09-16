package com.example.smartparking.ui.parking.navigation.result

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.TripDetails
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.databinding.NavigationResultFragmentBinding
import com.example.smartparking.internal.DEFAULT_BIKE_WALK_TIME
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.internal.NavigationDetailsNotFoundException
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import kotlinx.android.synthetic.main.directions_info_bike.*
import kotlinx.android.synthetic.main.directions_info_car.*
import kotlinx.android.synthetic.main.directions_info_walk.*
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class NavigationResultFragment : ScopedFragment() {
    private lateinit var binding: NavigationResultFragmentBinding
    private lateinit var navDetails: NavigationDetails
    private val navigationResultViewModel: NavigationResultViewModel by viewModels()
    private var mapsButton: Button? = null
    private var carButton: LinearLayout? = null
    private var bikeButton: LinearLayout? = null
    private var walkButton: LinearLayout? = null
    private var requestDirectionDataCar = DirectionData()
    private var requestDirectionDataBike = DirectionData()
    private var selectedBubbles = ArrayList<BubbleListModel>()
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var originPosition : String
    private lateinit var bubbleAdapter : BubbleSelectedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_result_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationResultViewModel = navigationResultViewModel
        val safeArgs = arguments?.let { NavigationResultFragmentArgs.fromBundle(it) }
        navDetails = safeArgs?.navigationDetails ?: throw NavigationDetailsNotFoundException()

        Log.d(TAG, "Navigation data: ${navDetails.room}")
        requestDirectionDataCar.destinations = "${navDetails.room.parking_latitude},${navDetails.room.parking_longitude}"
        requestDirectionDataBike.destinations = "${navDetails.room.latitude},${navDetails.room.longitude}"
        selectedBubbles = navDetails.bubbleStops as ArrayList<BubbleListModel>

        initProgressBar(requireContext())

        initTitle()
        bindUI(navDetails)
        initMapsButton(navDetails)
        initBubbleSelected()
        initNavigationButtons()
    }

    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "Route to ${navDetails.room}"
        }
    }

    private fun initNavigationButtons() {
        carButton = view?.findViewById(R.id.ll_car_button)
        carButton?.setOnClickListener {view ->
            sendNavigationDetails(view, TransportMode.DRIVING)
        }

        bikeButton = view?.findViewById(R.id.ll_bike_button)
        bikeButton?.setOnClickListener {view ->
            sendNavigationDetails(view, TransportMode.BICYCLING)
        }

        walkButton = view?.findViewById(R.id.ll_walk_button)
        walkButton?.setOnClickListener {view ->
            sendNavigationDetails(view, TransportMode.WALKING)
        }
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

    private fun initBubbleSelected(){
        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }


    private fun sendNavigationDetails(view: View, transportMode: TransportMode) {
        var infoText : String
        var timeTrip: String
        when (transportMode) {
            TransportMode.DRIVING -> {infoText = tv_car_text.text.toString()
            timeTrip = tv_car_result.text.toString()}
            TransportMode.BICYCLING -> {infoText = tv_bike_text.text.toString()
                timeTrip = tv_bike_result.text.toString()}
            TransportMode.WALKING -> {infoText = "walk a bit"
                timeTrip = tv_walk_result.text.toString()}
        }

        val tripDetail = TripDetails(transportMode, infoText, timeTrip, "LOW", selectedBubbles)
        val actionDetail = NavigationResultFragmentDirections.actionToTrip(tripDetail)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun initMapsButton(navDetails: NavigationDetails) {
//        mapsButton = view?.findViewById(R.id.btn_googleMaps)
        mapsButton?.setOnClickListener {
            val builder = Uri.Builder()
            builder.scheme("https")
                .authority("www.google.com")
                .path("maps/dir/")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin", originPosition)
                .appendQueryParameter("destination", "${navDetails.room.latitude},${navDetails.room.longitude}")
                .appendQueryParameter("travelmode", "driving")
                .appendQueryParameter("arrival_time", navDetails.time)
                .appendQueryParameter("traffic_mode", "pessimistic")

            val addressUri = builder.build()

            Log.d(TAG, "My url $addressUri")

            val intent = Intent(Intent.ACTION_VIEW, addressUri)
            intent.setPackage("com.android.chrome")
            startActivity(intent)
        }
    }

    private fun bindUI(navDetails: NavigationDetails) = launch {
        navigationResultViewModel.getNavigationData(requestDirectionDataCar, requestDirectionDataBike)
        navigationResultViewModel.navigationDataAll.observe(viewLifecycleOwner,
            Observer {
                if ((it.driving == null) || (it.bicycling == null)) {
                    return@Observer
                }
                loadingDialog.dismiss()

                setExpandTextCar(it.driving)
                setExpandTextBike(it.bicycling)
            })

        navigationResultViewModel.gpsOrigin.observe(viewLifecycleOwner,
            Observer { gpsPos -> originPosition = gpsPos
        })

//        tv_destination_result.setText(navDetails.room.name).toString()
    }

    private fun overallTime(googleResult: Duration?, minutes: Duration): Int {
        var overallTime = googleResult!!.plus(minutes)
        return overallTime!!.inWholeMinutes.toInt()
    }

    private fun setExpandTextCar(googleResult: Duration?) {
        tv_car_result.text = "${overallTime(googleResult, navDetails.room.walking_distance.minutes)} min".uppercase()
        tv_car_text.text = "Drive ${googleResult!!.inWholeMinutes} minutes, park in ${navDetails.room.parking} " +
                "(2/10 available) then walk ${navDetails.room.walking_distance} minutes"
    }

    private fun setExpandTextBike(googleResult: Duration?) {
        tv_bike_result.text = "${overallTime(googleResult, DEFAULT_BIKE_WALK_TIME)} min".uppercase()
        tv_bike_text.text = "Ride ${googleResult!!.inWholeMinutes} minutes, park in ${navDetails.room.parking} " +
                "(39/50 available) then walk ${DEFAULT_BIKE_WALK_TIME.inWholeMinutes.toInt()} minutes"
    }
}