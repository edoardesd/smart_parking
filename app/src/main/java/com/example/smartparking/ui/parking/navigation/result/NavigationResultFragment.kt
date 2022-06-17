package com.example.smartparking.ui.parking.navigation.result

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.network.*
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.databinding.NavigationResultFragmentBinding
import com.example.smartparking.internal.NavigationDetailsNotFoundException
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NavigationResultFragment : Fragment() {

    private lateinit var binding: NavigationResultFragmentBinding
    private val navigationResultViewModel: NavigationResultViewModel by viewModels()
    private var mapsButton: Button? = null
    private var requestDirectionData = DirectionData()

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
        val navDetails = safeArgs?.navigationDetails ?: throw NavigationDetailsNotFoundException()
        Log.d(TAG, "Navigation data: ${navDetails.room}")
        requestDirectionData.origins = "46.79542968182268,9.8245288366505"
        requestDirectionData.destinations = "${navDetails.room.latitude},${navDetails.room.longitude}"

        bindUI(navDetails)
        initMapsButton(navDetails)
        initExpandButtons(navDetails)

    }

    private fun initExpandButtons(navDetails: NavigationDetails) {
        car_expand_btn.setOnClickListener {
            if(expandable_l_car.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(car_card_view, AutoTransition())
                expandable_l_car.visibility = View.VISIBLE
                car_expand_btn.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(car_card_view, AutoTransition())
                expandable_l_car.visibility = View.GONE
                car_expand_btn.text = "EXPAND"
            }
        }

        bike_expand_btn.setOnClickListener {
            if(expandable_l_bike.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(bike_card_view, AutoTransition())
                expandable_l_bike.visibility = View.VISIBLE
                bike_expand_btn.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(bike_card_view, AutoTransition())
                expandable_l_bike.visibility = View.GONE
                bike_expand_btn.text = "EXPAND"
            }
        }
    }

    private fun initMapsButton(navDetails: NavigationDetails) {
        mapsButton = view?.findViewById(R.id.btn_googleMaps)
        mapsButton?.setOnClickListener {
            val builder = Uri.Builder()
            builder.scheme("https")
                .authority("www.google.com")
                .path("maps/dir/")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin","46.79542968182268,9.8245288366505")
                .appendQueryParameter("destination", "${navDetails.room.latitude},${navDetails.room.longitude}")
                .appendQueryParameter("travelmode", "bicycling")

            val addressUri = builder.build()

            Log.d(TAG, "My url $addressUri")

            val intent = Intent(Intent.ACTION_VIEW, addressUri)
            startActivity(intent)

        }
    }

    private fun bindUI(navDetails: NavigationDetails) {
        navigationResultViewModel.getDataCar(requestDirectionData)
        navigationResultViewModel.navigationDataCar.observe(viewLifecycleOwner,
            Observer { navigationTime -> tv_car_result.text = navigationTime })

        navigationResultViewModel.getDataBicycle(requestDirectionData)
        navigationResultViewModel.navigationDataBike.observe(viewLifecycleOwner,
            Observer { navigationTime -> tv_bike_result.text = navigationTime })

        tv_destination_result.setText(navDetails.room.name).toString()
    }


}