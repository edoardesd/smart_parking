package com.example.smartparking.ui.parking.control.disabled

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.smartparking.R

class ControlDisabledFragment : Fragment() {

    private var travelButton : Button? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.control_disabled_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTravelButton()
    }

    private fun initTravelButton() {
        travelButton = view?.findViewById(R.id.btn_travel)
        travelButton?.setOnClickListener {view ->
            Navigation.findNavController(view).navigate(R.id.navigationChoiceFragment)
        }
    }

}