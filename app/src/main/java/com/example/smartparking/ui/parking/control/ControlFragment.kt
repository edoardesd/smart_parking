package com.example.smartparking.ui.parking.control

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.smartparking.R
import com.example.smartparking.databinding.ControlFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.control_fragment.*
import kotlinx.android.synthetic.main.navigation_choice_fragment.*


class ControlFragment : ScopedFragment() {

    private lateinit var loadingDialog : LoadingDialog
    private lateinit var binding: ControlFragmentBinding
    private val controlViewModel: ControlViewModel by viewModels()

    private var selectedIndexParking : Int = 0


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

        initProgressBar(requireContext())
        initStream()
        initFreeSlots()
    }

    override fun onResume() {
        super.onResume()
        initDropdownMenu()
    }

    private fun initDropdownMenu() {
        val parkingLocations = resources.getStringArray(R.array.parking_location)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_parking, parkingLocations)

        binding.actvParkingLocation.setAdapter(arrayAdapter)

        actv_parking_location.setOnItemClickListener { _, _, position, _ ->
            selectedIndexParking = position
            Log.d(ContentValues.TAG, "Selected parking at position: $position")
        }
    }

    private fun initFreeSlots() {
        controlViewModel.slotsPrediction.observe(viewLifecycleOwner, Observer { freeSlots ->
            if(freeSlots == null) return@Observer
            txt_free_slots.text = freeSlots.toString()
        })
    }

    private fun initStream() {
        controlViewModel.bitmapDASTU.observe(viewLifecycleOwner, Observer { bitmap ->
            if ( bitmap == null) return@Observer
            if (selectedIndexParking == 1){
                loadingDialog.dismiss()
                iv_mqtt.setImageBitmap(bitmap)
                Log.d(TAG, "GET dastu")
                Log.d(TAG, "$bitmap")
                Log.d(TAG, "selected parking $selectedIndexParking")
            }
        })

        controlViewModel.bitmapDEIB.observe(viewLifecycleOwner, Observer { bitmap ->
            if ( bitmap == null) return@Observer
            if (selectedIndexParking == 0) {
                loadingDialog.dismiss()
                iv_mqtt.setImageBitmap(bitmap)
                Log.d(TAG, "GET deib")
                Log.d(TAG, "$bitmap")
                Log.d(TAG, "selected parking $selectedIndexParking")

            }
        })
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

}