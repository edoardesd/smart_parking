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
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.NavigationChoiceFragmentDirections
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.result.recyclers.BubbleSelectedAdapter
import com.example.smartparking.ui.parking.navigation.trip.NavigationTripFragment
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class NavigationResultFragment : ScopedFragment() {

//    override val kodein by closestKodein()
//    private val viewModelFactory : NavigationResultViewModelFactory by instance()

//    private lateinit var viewModel: NavigationResultViewModel
    private lateinit var binding: NavigationResultFragmentBinding
    private lateinit var navDetails: NavigationDetails
    private val navigationResultViewModel: NavigationResultViewModel by viewModels()
    private var mapsButton: Button? = null
    private var carButton: LinearLayout? = null
    private var requestDirectionDataCar = DirectionData()
    private var requestDirectionDataBike = DirectionData()
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var originPosition : String
    private lateinit var bubbleAdapter : BubbleSelectedAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_result_fragment, container, false)
        return binding.root
    //        return inflater.inflate(R.layout.navigation_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationResultViewModel = navigationResultViewModel
        val safeArgs = arguments?.let { NavigationResultFragmentArgs.fromBundle(it) }
        navDetails = safeArgs?.navigationDetails ?: throw NavigationDetailsNotFoundException()

        Log.d(TAG, "Navigation data: ${navDetails.room}")
        requestDirectionDataCar.destinations = "${navDetails.room.parking_latitude},${navDetails.room.parking_longitude}"
        requestDirectionDataBike.destinations = "${navDetails.room.latitude},${navDetails.room.longitude}"

        Log.d(TAG, "$requestDirectionDataCar")
        Log.d(TAG, "$requestDirectionDataBike")
        initProgressBar(requireContext())

        bindUI(navDetails)
        initMapsButton(navDetails)
        initBubbleSelected()
        initNavigationButtons()
    }

    private fun initNavigationButtons() {
        carButton = view?.findViewById(R.id.ll_car_button)
        carButton?.setOnClickListener {view ->
            sendNavigationDetails(view)
        }
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

    private fun initBubbleSelected(){
        var selectedBubbles = ArrayList<BubbleListModel>()

        selectedBubbles.add(BubbleListModel("Bar", R.drawable.bar))
        selectedBubbles.add(BubbleListModel("Library",R.drawable.library))
        selectedBubbles.add(BubbleListModel("Microwaves", R.drawable.microwaves))

        bubbleAdapter = BubbleSelectedAdapter(selectedBubbles)
        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles_selected)
        val bubblesLayoutManager = LinearLayoutManager(requireContext())
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter
    }


    private fun sendNavigationDetails(view: View) {
        val tripDetail = TripDetails("car", "asdasd")
        val actionDetail = NavigationResultFragmentDirections.actionToTrip(tripDetail)

        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun initMapsButton(navDetails: NavigationDetails) {
        mapsButton = view?.findViewById(R.id.btn_googleMaps)
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

                Log.d(TAG, "Enable navigation result view")

                group_loading_result.visibility = View.GONE
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
        tv_bike_result.text = "${overallTime(googleResult, DEFAULT_BIKE_WALK_TIME)} minutes"
        tv_bike_text.text = "Ride ${googleResult!!.inWholeMinutes} minutes, park in ${navDetails.room.parking} " +
                "(39/50 available) then walk ${DEFAULT_BIKE_WALK_TIME.inWholeMinutes.toInt()} minutes"

//        tv_bike_opt1.text = "1) Ride for ${googleResult!!.inWholeMinutes} minutes"
//        var myString = "Leave the bike in front of ${navDetails.room.name}. \nParking expectation: <font color='#00FF00'>very high</font>."
//        tv_bike_opt2.text = Html.fromHtml(myString)
    }
}