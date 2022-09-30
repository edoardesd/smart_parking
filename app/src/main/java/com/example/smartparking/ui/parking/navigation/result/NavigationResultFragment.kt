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
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.TripDetails
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalDestinationInfo
import com.example.smartparking.databinding.NavigationResultFragmentBinding
import com.example.smartparking.internal.*
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import kotlinx.android.synthetic.main.directions_info_bike.*
import kotlinx.android.synthetic.main.directions_info_car.*
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.time.Duration


class NavigationResultFragment : ScopedFragment() {
    private lateinit var binding: NavigationResultFragmentBinding
    private lateinit var navDetails: NavigationDetails
    private val navigationResultViewModel: NavigationResultViewModel by viewModels()
    private var mapsButton: Button? = null
    private var carButton: LinearLayout? = null
    private var bikeButton: LinearLayout? = null

    //    private var walkButton: LinearLayout? = null
    private var requestDirectionDataCar = DirectionData()
    private var requestDirectionDataBike = DirectionData()
    private var selectedBubbles = ArrayList<BubbleListModel>()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var originPosition: String
    private lateinit var bubbleAdapter: BubbleSelectedAdapter
    private lateinit var selectedLesson: LessonListModel
    private lateinit var startTime: MyDate
    private lateinit var infoCar: InfoText
    private lateinit var infoBike: InfoText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.navigation_result_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationResultViewModel = navigationResultViewModel

        val safeArgs = arguments?.let { NavigationResultFragmentArgs.fromBundle(it) }
        arguments?.clear()

        if (safeArgs?.navigationDetails != null) {
            navDetails = safeArgs?.navigationDetails ?: throw NavigationDetailsNotFoundException()
            navigationResultViewModel.navigationDetails.value = navDetails
        } else {
            navDetails = navigationResultViewModel.navigationDetails.value!!
        }
        selectedLesson = navDetails.lesson as LessonListModel
        startTime = navDetails.time as MyDate

        requestDirectionDataCar.destinations = selectedLesson.coordinates
        requestDirectionDataBike.destinations = selectedLesson.coordinates
        selectedBubbles = navDetails.bubbleStops as ArrayList<BubbleListModel>



        initProgressBar(requireContext())

        initTitle()
        bindUI(navDetails)
//        initMapsButton(navDetails)
        initBubbleSelected()
        initNavigationButtons()
    }

    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "Route to ${selectedLesson.title}"
        }
    }

    private fun initNavigationButtons() {
        carButton = view?.findViewById(R.id.ll_car_button)
        carButton?.setOnClickListener { view ->
            sendNavigationDetails(view, TransportMode.DRIVING)
        }

        bikeButton = view?.findViewById(R.id.ll_bike_button)
        bikeButton?.setOnClickListener { view ->
            sendNavigationDetails(view, TransportMode.BICYCLING)
        }
//
//        walkButton = view?.findViewById(R.id.ll_walk_button)
//        walkButton?.setOnClickListener { view ->
//            sendNavigationDetails(view, TransportMode.WALKING)
//        }
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

    private fun initBubbleSelected() {
        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }


    private fun sendNavigationDetails(view: View, transportMode: TransportMode) {
        // pass data to trip fragment
        globalDestinationInfo = getInfoText(transportMode)
        val tripDetail = TripDetails(getInfoText(transportMode), selectedLesson, selectedBubbles)
        val actionDetail = NavigationResultFragmentDirections.actionToTrip(tripDetail)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun getInfoText(transportMode: TransportMode): InfoText {
        return when (transportMode) {
            TransportMode.DRIVING -> infoCar
            TransportMode.BICYCLING -> infoBike
            TransportMode.WALKING -> infoCar
        }
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
                .appendQueryParameter(
                    "destination",
                    requestDirectionDataCar.destinations
                )
                .appendQueryParameter("travelmode", "driving")
                .appendQueryParameter("arrival_time", startTime.epoch)
                .appendQueryParameter("traffic_mode", "pessimistic")

            val addressUri = builder.build()

            Log.d(TAG, "My url $addressUri")

            val intent = Intent(Intent.ACTION_VIEW, addressUri)
            intent.setPackage("com.android.chrome")
            startActivity(intent)
        }
    }

    private fun bindUI(navDetails: NavigationDetails) = launch {
        navigationResultViewModel.getNavigationData(
            requestDirectionDataCar,
            requestDirectionDataBike,
            navDetails
        )
        navigationResultViewModel.navigationDataAll.observe(viewLifecycleOwner,
            Observer {
                if ((it.driving == null) || (it.bicycling == null)) {
                    return@Observer
                }
                setExpandTextCar(it.driving)
                setExpandTextBike(it.bicycling)
                Log.d(TAG, "Expand bike ${it.bicycling}")
                loadingDialog.dismiss()

            })

        navigationResultViewModel.gpsOrigin.observe(viewLifecycleOwner,
            Observer { gpsPos ->
                originPosition = gpsPos
                tv_current_location.text = navigationResultViewModel.getLocationString()
            })
    }

    private fun setExpandTextCar(googleResult: Duration?) {
        infoCar = navigationResultViewModel.infoTextCar.value!!
        infoCar.infoTransportTime.startTime = startTime
        setBoxColor(ll_car_button, infoCar.infoTransportTime.availability)
        infoCar.infoTransportTime.transportTime = googleResult!!
        tv_car_result.text = infoCar.totalTimeText()
        tv_car_text.text = infoCar.fullText()
        tv_availability_car.text = infoCar.infoTransportTime.availability.name.uppercase()
    }

    private fun setExpandTextBike(googleResult: Duration?) {
        infoBike = navigationResultViewModel.infoTextBike.value!!
        setBoxColor(ll_bike_button, infoBike.infoTransportTime.availability)
        infoBike.infoTransportTime.transportMode = TransportMode.BICYCLING
        infoBike.infoTransportTime.startTime = startTime
        infoBike.infoTransportTime.transportTime = googleResult!!
        tv_bike_result.text = infoBike.totalTimeText()
        tv_bike_text.text = infoBike.fullText()
        tv_availability_bike.text = infoBike.infoTransportTime.availability.name.uppercase()
    }

//    private fun setBoxColor(layout: LinearLayout?, availability: ParkingAvailability) {
//        when (availability) {
//            ParkingAvailability.LOW -> layout?.setBackgroundResource(R.drawable.rectangle_gray)
//            ParkingAvailability.MEDIUM -> layout?.setBackgroundResource(R.drawable.rectangle_blue)
//            ParkingAvailability.HIGH -> layout?.setBackgroundResource(R.drawable.rectangle_green)
//        }
//    }

}