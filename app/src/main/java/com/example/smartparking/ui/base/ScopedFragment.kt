package com.example.smartparking.ui.base

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.smartparking.R
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.internal.ParkingAvailability
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun setBoxColor(layout: LinearLayout?, availability: ParkingAvailability) {
        when(availability){
            ParkingAvailability.LOW -> layout?.setBackgroundResource(R.drawable.rectangle_gray)
            ParkingAvailability.MEDIUM -> layout?.setBackgroundResource(R.drawable.rectangle_blue)
            ParkingAvailability.HIGH -> layout?.setBackgroundResource(R.drawable.rectangle_green)
        }
    }

}