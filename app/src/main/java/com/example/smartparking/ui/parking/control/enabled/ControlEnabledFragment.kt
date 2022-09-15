//package com.example.smartparking.ui.parking.control.enabled
//
//import android.content.ContentValues
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import androidx.navigation.Navigation
//import com.example.smartparking.R
//import com.example.smartparking.data.db.SmartParkingApplication
//import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
//import com.example.smartparking.databinding.ControlEnabledFragmentBinding
//import com.example.smartparking.internal.LoadingDialog
//import com.example.smartparking.ui.base.ScopedFragment
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlinx.android.synthetic.main.control_enabled_fragment.*
//
//
//class ControlEnabledFragment : ScopedFragment() {
//
//    private lateinit var loadingDialog : LoadingDialog
//    private lateinit var binding: ControlEnabledFragmentBinding
//    private val controlViewModel: ControlViewModel by viewModels()
//
//    private var selectedIndexParking : Int = 0
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.control_enabled_fragment, container, false)
//        return binding.root
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////        binding.controlViewModel = controlViewModel
//
//        if (!globalIsParking) {
//            Log.d(ContentValues.TAG, "global parking $globalIsParking")
//            Navigation.findNavController(requireView()).navigate(R.id.controlDisabledFragment)
//        }
//
//        initProgressBar(requireContext())
//        initStream()
//
//
//
//        val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav) as BottomNavigationView
//        bottomNavigationView.menu.findItem(R.id.controlDisabledFragment).isChecked = true
////        initFreeSlots()
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//    }
//
////    private fun initDropdownMenu() {
////        val parkingLocations = resources.getStringArray(R.array.parking_location)
////        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_parking, parkingLocations)
////
////        binding.actvParkingLocation.setAdapter(arrayAdapter)
////
////        actv_parking_location.setOnItemClickListener { _, _, position, _ ->
////            selectedIndexParking = position
////            controlViewModel.getSelectedParking(parkingLocations[position])
////            Log.d(TAG, "Selected parking at position: ${parkingLocations[position]} $position")
////        }
////    }
//
////    private fun initFreeSlots() {
////        controlViewModel.slotsPrediction.observe(viewLifecycleOwner, Observer { freeSlots ->
////            if(freeSlots == null) return@Observer
////            txt_free_slots.text = "$freeSlots /7"
////        })
////    }
//
//    private fun initStream() {
//        controlViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
//            if ( bitmap == null) return@Observer
//                loadingDialog.dismiss()
//                iv_parking_pic.setImageBitmap(bitmap)
//        })
//    }
//
//    private fun initProgressBar(context: Context) {
//        loadingDialog = LoadingDialog(context)
//        loadingDialog.startLoading()
//    }
//
//}