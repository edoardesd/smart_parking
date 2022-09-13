package com.example.smartparking.ui.parking.navigation.choice

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.databinding.NavigationChoiceFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListAdapter
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListAdapter
import com.example.smartparking.ui.parking.navigation.choice.recyclers.LessonListModel
//import com.example.smartparking.ui.parking.navigation.result.NavigationResultFragmentDirections
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*


class NavigationChoiceFragment : ScopedFragment() {

    private lateinit var loadingDialog : LoadingDialog
    private lateinit var binding: NavigationChoiceFragmentBinding
    private val navigationChoiceViewModel: NavigationChoiceViewModel by viewModels()

    private var timeButton: Button? = null
    private var dateButton: Button? = null
    private var goButton: Button? = null

    private lateinit var bubbleAdapter : BubbleListAdapter
    private lateinit var lessonAdapter: LessonListAdapter
    private var date = MyDate()

    private var selectedIndex : Int = 0

    private var bubblesSelected = ArrayList<BubbleListModel>()

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

        initBubbleView()
        initLessonView()
        initTimePicker()
        initDatePicker()
        initGoButton()
        initTextView()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_item, menu)
//        val item = menu.findItem(R.id.search_action)
//
//        val searchView = item.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val searchText = newText!!.lowercase(Locale.getDefault())
//                if (searchText.isNotEmpty()){
//                    Log.d("TAG", "$searchText")
//                } else {
//                    Log.d(TAG, "empty")
//                }
//                return false
//            }
//        })
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    private fun initBubbleView(){
        //TODO get from db
        var listBubbles = ArrayList<BubbleListModel>()

        listBubbles.add(BubbleListModel("Bar", R.drawable.bar))
        listBubbles.add(BubbleListModel("Library",R.drawable.library))
        listBubbles.add(BubbleListModel("Microwaves", R.drawable.microwaves))
        listBubbles.add(BubbleListModel("Park", R.drawable.park))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets))
        listBubbles.add(BubbleListModel("Study Room", R.drawable.study_room))
        listBubbles.add(BubbleListModel("Toilets", R.drawable.toilets))

        bubbleAdapter = BubbleListAdapter(listBubbles)

        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles)
        val bubblesLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter

        bubbleAdapter.bubbleSelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                bubbles -> if (bubbles == null) return@Observer
            Log.d(TAG, "selected this $bubbles")
            bubblesSelected = bubbles
            Log.d(TAG, "bubble list $bubblesSelected")

        })
    }

    private fun initLessonView(){
        var listLessons = ArrayList<LessonListModel>()

        listLessons.add(LessonListModel("Lesson blbla", "room 2", "monday 10-15", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 2", "room 1", "monday 12-15", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 3", "room 4", "monday 16-18", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 4", "room 5", "monday 10-15", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 5", "room 21", "monday 10-15", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 6", "room 23", "monday 10-15", R.drawable.ic_map_indoor_background))
        listLessons.add(LessonListModel("Lesson blbla 7", "room 45", "monday 10-15", R.drawable.ic_map_indoor_background))

        lessonAdapter = LessonListAdapter(listLessons)
        var recyclerLessons = view?.findViewById<RecyclerView>(R.id.rv_lessons)
        val lessonsLayoutManager = LinearLayoutManager(requireContext())
        recyclerLessons?.layoutManager = lessonsLayoutManager
        recyclerLessons?.adapter = lessonAdapter

        //capture lesson change
        lessonAdapter.lessonSelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                lesson -> if (lesson == null) return@Observer
                Log.d(TAG, "selected this $lesson")
            // TODO change date/time pass to next fragment
        })
    }

    private fun initGoButton() {
        goButton = view?.findViewById(R.id.btn_go)
        goButton?.setOnClickListener {view ->
            sendNavigationDetails(view)
        }
    }

    private fun sendNavigationDetails(view: View) {
        Log.d(TAG, "bubble list to send $bubblesSelected")
        val selectedLocation = binding.navigationChoiceViewModel?.getSelectedLocation(selectedIndex)
        val navigationDetail = NavigationDetails(selectedLocation!!, date.epoch, bubblesSelected)
        val actionDetail = NavigationChoiceFragmentDirections.actionResult(navigationDetail)

        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun initTimePicker() {
        timeButton = view?.findViewById(R.id.btn_time)
        timeButton?.text = String.format("now")
        timeButton?.setOnClickListener{
            openTimePicker()
        }
    }

    private fun initDatePicker() {
        dateButton = view?.findViewById(R.id.btn_date)
        dateButton?.setOnClickListener {
            openDatePicker()
        }
    }

    private fun initTextView() {
        navigationChoiceViewModel.rooms.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { roomsList ->
                if (roomsList == null) return@Observer

                loadingDialog.dismiss()

//                tv_auto_complete_text_view.setAdapter(
//                    ArrayAdapter(
//                        requireView().context,
//                        R.layout.list_item,
//                        roomsList))
            })

//        tv_auto_complete_text_view.setOnItemClickListener { _, _, position, _ ->
//          selectedIndex = position
//            Log.d(TAG, "Selected item at position: $position")
//        }
    }

    private fun openTimePicker(){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(date.hour)
            .setMinute(date.minutes)
            .setTitleText("Select Departure Time")
            .build()

        timePicker.show(childFragmentManager, "TAG")

        timePicker.addOnPositiveButtonClickListener {
            date.minutes = timePicker.hour
            date.hour = timePicker.minute
            updateTimeText(timePicker.hour, timePicker.minute)
            Log.d(TAG, "epoch: ${date.getEpochAsString()}")
        }
    }

    private fun openDatePicker(){
        val dateValidator: DateValidator = DateValidatorPointForward.from(date.timeMillis)
        val constraintsBuilder = CalendarConstraints.Builder().setValidator(dateValidator)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Departure Date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(childFragmentManager, "TAG")

        datePicker.addOnPositiveButtonClickListener {
            val dateSelected = Date(it)
            val sdf = SimpleDateFormat("dd MMM")
            dateButton?.text = sdf.format(dateSelected)
        }
    }

    private fun updateTimeText(_hour: Int, _minute: Int) {
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
