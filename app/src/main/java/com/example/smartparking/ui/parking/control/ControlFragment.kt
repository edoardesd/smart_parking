package com.example.smartparking.ui.parking.control

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.smartparking.R
import com.example.smartparking.data.db.InfoText
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalDestinationInfo
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.databinding.ControlFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.internal.ParkingLots
import com.example.smartparking.internal.TransportMode
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_enabled.*
import kotlinx.android.synthetic.main.navigation_trip_fragment.*


class ControlFragment : ScopedFragment() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: ControlFragmentBinding
    private lateinit var destinationInfo : InfoText
    private val controlViewModel: ControlViewModel by viewModels()
    private var travelButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.control_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.controlViewModel = controlViewModel

//        bottom_nav.selectedItemId = R.id.controlDisabledFragment

        val stub = view?.findViewById(R.id.control_stub) as ViewStub

        if(globalIsParking){
        stub.layoutResource = R.layout.control_enabled
        }
        else{
            stub.layoutResource = R.layout.control_disabled
        }
        stub.inflate()

        initTitle()

        if (globalIsParking) {
            destinationInfo = globalDestinationInfo
            initProgressBar(requireContext())
            controlViewModel.initializePark()
            initStream()
            initUI()
            Log.d(TAG, "global ${destinationInfo.infoTransportTime.transportMode}")
        } else {
            initTravelButton()
        }
    }

    private fun initUI() {
        tv_parking_name.text = "Parking Via ${destinationInfo.infoTransportTime.parkingLot.name.lowercase().capitalize()}"
        tv_parking_availability.text = "${destinationInfo.infoTransportTime.availability.name.lowercase().capitalize()} parking availability"
        tv_parked_here.text = "You parked here ${(3..15).random()} minutes ago."

        when (destinationInfo.infoTransportTime.transportMode){
            TransportMode.DRIVING -> iv_transport_type.setImageResource(R.drawable.ic_car_blue)
            TransportMode.BICYCLING -> iv_transport_type.setImageResource(R.drawable.ic_bike_blue)
            TransportMode.WALKING -> iv_transport_type.setImageResource(R.drawable.ic_walk_blue)
        }
    }


    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "Monitor"
            (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(false)
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun initTravelButton() {
        travelButton = view?.findViewById(R.id.btn_travel)
        travelButton?.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.navigationChoiceFragment)
        }
    }

    private fun initStream() {
        controlViewModel.updateImage(destinationInfo.infoTransportTime.parkingLot)
        controlViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
            if (bitmap == null) return@Observer
            loadingDialog.dismiss()
//            controlViewModel.updateImage(destinationInfo.infoTransportTime.parkingLot)
            iv_parking_pic.setImageBitmap(bitmap)
        })
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

}