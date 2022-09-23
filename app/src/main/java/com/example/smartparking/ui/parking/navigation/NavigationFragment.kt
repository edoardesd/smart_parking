package com.example.smartparking.ui.parking.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication

class NavigationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (SmartParkingApplication.globalIsParking) {
            true -> Navigation.findNavController(requireView()).navigate(R.id.navigationTripFragment)
            false -> Navigation.findNavController(requireView()).navigate(R.id.navigationChoiceFragment)
        }
    }
}