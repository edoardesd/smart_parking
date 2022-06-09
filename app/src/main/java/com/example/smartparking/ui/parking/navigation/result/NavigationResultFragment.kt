package com.example.smartparking.ui.parking.navigation.result

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.network.GoogleAPIService
import com.example.smartparking.data.provider.UnitProvider
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
        val apiService = GoogleAPIService()

        GlobalScope.launch(Dispatchers.Main) {
            val carAPIResponse = apiService?.getGoogleDirection("46.79542968182268,9.8245288366505",
                "${navDetails.room.latitude},${navDetails.room.longitude}",
                "false", "metrics",
                "driving")
                ?.await()

            val bikeAPIResponse = apiService?.getGoogleDirection("46.79542968182268,9.8245288366505",
                "${navDetails.room.latitude},${navDetails.room.longitude}",
                "false", "metrics",
                "bicycling")
                ?.await()

            // TODO: check if response is not null
            Log.d(TAG, carAPIResponse.toString())
            tv_car_result.text = carAPIResponse?.rows?.first()?.elements?.first()?.duration?.text
            tv_bike_result.text = bikeAPIResponse?.rows?.first()?.elements?.first()?.duration?.text
        }
        tv_destination_result.setText(navDetails.room.name).toString()
    }


}