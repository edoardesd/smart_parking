package com.example.smartparking.ui.parking.control.disabled

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
import com.example.smartparking.databinding.ControlDisabledFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.ui.base.ScopedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_enabled.*
import kotlinx.android.synthetic.main.navigation_trip_fragment.*

class ControlDisabledFragment : ScopedFragment() {

    private lateinit var loadingDialog: LoadingDialog

    private var travelButton: Button? = null
    private var layout: Int = R.layout.control_disabled_fragment

    private lateinit var binding: ControlDisabledFragmentBinding
    private val controlDisabledViewModel: ControlDisabledViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        if (globalIsParking) {
//            layout = R.layout.control_disabled_fragment
//        }

//        val bottomNavigationView: BottomNavigationView = view?.findViewById(R.id.bottom_nav) as BottomNavigationView
//        bottomNavigationView.selectedItemId = R.id.controlDisabledFragment


        Log.d(TAG, "arriving here")
        binding = DataBindingUtil.inflate(inflater, R.layout.control_disabled_fragment, container, false)

        Log.d(TAG, "on activity binding")

        return binding.root
//        return inflater.inflate(layout, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "on activity created before")

        binding.controlDisabledViewModel = controlDisabledViewModel

//        bottom_nav.selectedItemId = R.id.controlDisabledFragment

        val stub = view?.findViewById(R.id.control_stub) as ViewStub

        if(globalIsParking){
        stub.layoutResource = R.layout.control_enabled
        }
        else{
            stub.layoutResource = R.layout.control_disabled

    }
        stub.inflate()
        Log.d(TAG, "on activity created")

//        if (globalIsParking) {
//            Log.d(TAG, "global parking $globalIsParking")
//            Navigation.findNavController(requireView()).navigate(R.id.controlEnabledFragment)
//        }

        if (globalIsParking) {
            initProgressBar(requireContext())
            initStream()
        } else {
            initTravelButton()

        }
    }

    private fun initTravelButton() {
        travelButton = view?.findViewById(R.id.btn_travel)
        travelButton?.setOnClickListener { view ->
//            activity?.bottom_nav?.selectedItemId = R.id.navigationChoiceFragment
            Navigation.findNavController(view).navigate(R.id.navigationChoiceFragment)
        }
    }

    private fun initStream() {
        controlDisabledViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
            if (bitmap == null) return@Observer
            loadingDialog.dismiss()
            iv_parking_pic.setImageBitmap(bitmap)
        })
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

}