package com.example.smartparking.ui.parking.navigation.choice

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.smartparking.R
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.RoomDetails
import com.example.smartparking.data.network.FirestoreService
import com.example.smartparking.data.network.choice.DatabaseNetworkDataSourceImpl
import com.example.smartparking.databinding.NavigationChoiceFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.navigation_choice_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class NavigationChoiceFragment : ScopedFragment() {

    private lateinit var loadingDialog : LoadingDialog
    private lateinit var binding: NavigationChoiceFragmentBinding
    private val navigationChoiceViewModel: NavigationChoiceViewModel by viewModels()

    private var timeButton: Button? = null
    private var goButton: Button? = null
    private var date = MyDate()

    private var selectedIndex : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_choice_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationChoiceViewModel = navigationChoiceViewModel

        initProgressBar(requireContext())

        initTimePicker()
        initGoButton()
        initTextView()
    }

    private fun initGoButton() {
        goButton = view?.findViewById(R.id.btn_go)
        goButton?.setOnClickListener {view ->
            sendNavigationDetails(view)
        }
    }

    private fun sendNavigationDetails(view: View) {
        val selectedLocation = binding.navigationChoiceViewModel?.getSelectedLocation(selectedIndex)
        val navigationDetail = NavigationDetails(selectedLocation!!, date.epoch)
        val actionDetail = NavigationChoiceFragmentDirections.actionResult(navigationDetail)

        with(Navigation) {
            findNavController(view).navigate(actionDetail)
        }
    }

    private fun initTimePicker() {
        timeButton = view?.findViewById(R.id.btn_time)
        timeButton?.text = String.format("now")
        timeButton?.setOnClickListener{
            openTimePicker()
        }
    }

    private fun initTextView() {
        navigationChoiceViewModel.rooms.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { roomsList ->
                if (roomsList == null) return@Observer

                loadingDialog.dismiss()

                tv_auto_complete_text_view.setAdapter(
                    ArrayAdapter(
                        requireView().context,
                        R.layout.list_item,
                        roomsList))
            })

        tv_auto_complete_text_view.setOnItemClickListener { _, _, position, _ ->
          selectedIndex = position
            Log.d(TAG, "Selected item at position: $position")
        }
    }

    private fun openTimePicker(){
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                date.minutes = selectedMinute
                date.hour = selectedHour
                updateText(selectedHour, selectedMinute)
                Log.d(TAG, "epoch: ${date.getEpochAsString()}")
            }

        val timePickerDialog =
            TimePickerDialog(requireView().context,
                AlertDialog.THEME_HOLO_DARK,  // set theme
                onTimeSetListener,
                date.hour,
                date.minutes,
                true)
        timePickerDialog.setTitle("Select Departure Time")
        timePickerDialog.show()

    }

    private fun updateText(_hour: Int, _minute: Int) {
        Log.d(TAG, "Hour $_hour, Minute $_minute")
        timeButton?.text = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            _hour,
            _minute,
        )
    }

    private fun initProgressBar(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.startLoading()
    }

}
