package com.example.smartparking.ui.parking.navigation.result

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.smartparking.R
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.DirectionData
import com.example.smartparking.data.network.*
import com.example.smartparking.data.network.result.NavigationNetworkDataSourceImpl
import com.example.smartparking.data.provider.LocationProvider
import com.example.smartparking.databinding.NavigationResultFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.internal.NavigationDetailsNotFoundException
import com.example.smartparking.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.control_fragment.*
import kotlinx.android.synthetic.main.navigation_result_fragment.*
import kotlinx.coroutines.launch


class NavigationResultFragment : ScopedFragment() {

//    override val kodein by closestKodein()
//    private val viewModelFactory : NavigationResultViewModelFactory by instance()

//    private lateinit var viewModel: NavigationResultViewModel
    private lateinit var binding: NavigationResultFragmentBinding
    private val navigationResultViewModel: NavigationResultViewModel by viewModels()
    private var mapsButton: Button? = null
    private var requestDirectionData = DirectionData()
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var originPosition : String


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
        requestDirectionData.destinations = "${navDetails.room.latitude},${navDetails.room.longitude}"

        initProgressBar(requireContext())

        bindUI(navDetails)
        initMapsButton(navDetails)
        initExpandButtons(navDetails)

    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
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
                .appendQueryParameter("origin", originPosition)
                .appendQueryParameter("destination", "${navDetails.room.latitude},${navDetails.room.longitude}")
                .appendQueryParameter("travelmode", "driving")
                .appendQueryParameter("arrival_time", navDetails.time)
                .appendQueryParameter("traffic_mode", "pessimistic")

            val addressUri = builder.build()

            Log.d(TAG, "My url $addressUri")

            val intent = Intent(Intent.ACTION_VIEW, addressUri)
            intent.setPackage("com.android.chrome")
            startActivity(intent)
        }
    }

    private fun bindUI(navDetails: NavigationDetails) = launch {
        navigationResultViewModel.getNavigationData(requestDirectionData)
        navigationResultViewModel.navigationDataAll.observe(viewLifecycleOwner,
            Observer {
                if ((it.driving == null) || (it.bicycling == null)) {
                    return@Observer
                }

                Log.d(TAG, "Enable navigation result view")

                group_loading_result.visibility = View.GONE
                loadingDialog.dismiss()

                tv_car_result.text = it.driving
                tv_bike_result.text = it.bicycling

            })

        navigationResultViewModel.gpsOrigin.observe(viewLifecycleOwner,
            Observer { gpsPos -> originPosition = gpsPos
        })

        tv_destination_result.setText(navDetails.room.name).toString()
    }


}