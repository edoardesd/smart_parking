package com.example.smartparking.ui.parking.navigation.result

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartparking.R
import com.example.smartparking.data.GoogleAPIService
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.internal.NavigationDetailsNotFoundException
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NavigationResultFragment : Fragment() {

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

            // TODO: check if response is null
            Log.d(TAG, carAPIResponse.toString())
            tv_car_result.text = carAPIResponse?.rows?.first()?.elements?.first()?.duration?.text
            tv_bike_result.text = bikeAPIResponse?.rows?.first()?.elements?.first()?.duration?.text
        }
        tv_destination_result.setText(navDetails.room.name).toString()
    }


}