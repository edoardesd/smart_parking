//package com.example.smartparking.ui.parking.control
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.navigation.Navigation
//import com.example.smartparking.R
//import com.example.smartparking.data.TripDetails
//import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalIsParking
//import com.example.smartparking.internal.TransportMode
//import com.example.smartparking.ui.parking.navigation.result.NavigationResultFragmentDirections
//import kotlinx.android.synthetic.main.navigation_result_fragment.*
//
//class ControlFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.control_disabled_fragment, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        when (globalIsParking) {
//            true -> Navigation.findNavController(requireView()).navigate(R.id.controlEnabledFragment)
//            false -> Navigation.findNavController(requireView()).navigate(R.id.controlDisabledFragment)
//        }
//    }
//
//}