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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.network.*
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.internal.NavigationDetailsNotFoundException
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NavigationResultFragment : Fragment() {

    private var mapsButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { NavigationResultFragmentArgs.fromBundle(it) }
        val navDetails = safeArgs?.navigationDetails ?: throw NavigationDetailsNotFoundException()
        Log.d(TAG, "Navigation data: ${navDetails.room}")

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
        val apiService = GoogleAPIService(ConnectivityInterceptorImpl(this.requireContext()))
        val navigationNetworkDataSourceCar = apiService?.let { NavigationNetworkDataSourceImpl(it) }
        val navigationNetworkDataSourceBike = apiService?.let { NavigationNetworkDataSourceImpl(it) }

        navigationNetworkDataSourceCar?.downloadedNavigation?.observe(viewLifecycleOwner, Observer {
            tv_car_result.text = it?.rows?.first()?.elements?.first()?.duration?.text
        })

        navigationNetworkDataSourceBike?.downloadedNavigation?.observe(viewLifecycleOwner, Observer {
            tv_bike_result.text = it?.rows?.first()?.elements?.first()?.duration?.text
        })

        GlobalScope.launch(Dispatchers.Main) {
            navigationNetworkDataSourceCar?.fetchedNavigation(
                "46.79542968182268,9.8245288366505",
                "${navDetails.room.latitude},${navDetails.room.longitude}",
                "false", "metrics",
                "driving")

            navigationNetworkDataSourceBike?.fetchedNavigation(
                "46.79542968182268,9.8245288366505",
                "${navDetails.room.latitude},${navDetails.room.longitude}",
                "false", "metrics",
                "bicycling")

            // TODO: check if response is not null
        }
        tv_destination_result.setText(navDetails.room.name).toString()
    }


}