package com.example.smartparking.ui.parking.navigation.choice

//import com.example.smartparking.ui.parking.navigation.result.NavigationResultFragmentDirections
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.LessonTime
import com.example.smartparking.data.MyDate
import com.example.smartparking.data.NavigationDetails
import com.example.smartparking.data.db.LessonDetails
import com.example.smartparking.data.recycleList.BubbleProvider
import com.example.smartparking.data.recycleList.LessonProvider
import com.example.smartparking.databinding.NavigationChoiceFragmentBinding
import com.example.smartparking.internal.LoadingDialog
import com.example.smartparking.ui.MainActivity
import com.example.smartparking.ui.base.ScopedFragment
import com.example.smartparking.ui.parking.navigation.choice.recyclers.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.FirebaseFirestore
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
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var recyclerLessons: RecyclerView
    private lateinit var recyclerSearch: RecyclerView
    private var date = MyDate()
    private lateinit var datetime: Date
    private var lessonSelected: LessonListModel? = null

    private var selectedIndex : Int = 0

    private var bubblesSelected = ArrayList<BubbleListModel>()

    private var listLessons = ArrayList<LessonListModel>()
    private var listBubbles = ArrayList<BubbleListModel>()
    private var searchLesson = ArrayList<LessonListModel>()
    private var displayLesson = ArrayList<LessonListModel>()

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_choice_fragment, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationChoiceViewModel = navigationChoiceViewModel

        setHasOptionsMenu(true)
//
//        if (SmartParkingApplication.globalIsParking) {
//            Navigation.findNavController(requireView()).navigate(R.id.navigationTripFragment)
//        }

        initTitle()
        initProgressBar(requireContext())

        initBubbleView()
        initLessonView()
        initSearchView()
        initTimePicker()
        initDatePicker()
        initGoButton()
//        initTextView()

    }

    private fun initSearchView() {
        var datetime = Date(2022, 8, 22)
        datetime.hours = 10
        datetime.minutes = 15

        searchLesson.add(LessonListModel("Room G.2", "Building 11 - Architettura","Aula didattica | disegno", LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Room C", "Building 11 - Architettura", "Aula didattica | platea frontale",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Room 16B.1.1", "Building 16B", "Aula didattica | disegno",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Room B.2.4", "Building 14 - NAVE", "Aula didattica | disegno",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Room L.26.16", "Building 26", "Aula didattica | platea frontale",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Place 6", "room 23", "carlo",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        searchLesson.add(LessonListModel("Place 7", "room 45", "carlo",LessonTime(datetime, datetime), R.drawable.ic_map_indoor_background))
        displayLesson.addAll(searchLesson)


        searchAdapter = SearchAdapter(searchLesson)
        recyclerSearch = view?.findViewById(R.id.rv_search)!!
        val searchLayoutManager = LinearLayoutManager(requireContext())
        recyclerSearch?.layoutManager = searchLayoutManager
        recyclerSearch?.adapter = searchAdapter
        recyclerSearch.isInvisible = true

        searchAdapter.searchSelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer { search ->
            if (search == null) return@Observer
            Log.d(TAG, "selected this from search ${search.title} ${search.lessonToString()}")

            listLessons.add(0, search)
            recyclerLessons?.adapter!!.notifyDataSetChanged()
            recyclerSearch.isInvisible = true
            searchView.clearFocus()
        })
    }

    private fun initTitle() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = "Parking"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "inflate the menu")
        inflater.inflate(R.menu.menu_item, menu)
        val menuItem = menu.findItem(R.id.action_search)

        if (menuItem != null) {
            Log.d(TAG, "inflate the menu which is not null")
            searchView = menuItem.actionView as SearchView
            searchView.queryHint = "Search"

            searchView.setOnQueryTextFocusChangeListener { _, newViewFocus ->
                recyclerSearch.isInvisible = !newViewFocus
                if (recyclerSearch.isInvisible){
                    searchView.clearFocus()
                }
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        searchLesson.clear()
                        val search = newText.lowercase(Locale.getDefault())
                        displayLesson.forEach{
                            if (it.title.lowercase(Locale.getDefault()).contains(search)){
                                searchLesson.add(it)
                            }
                        }
                        recyclerSearch?.adapter!!.notifyDataSetChanged()

                    }
                    else{
                        searchLesson.clear()
                        searchLesson.addAll(displayLesson)
                        recyclerSearch?.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })

            searchView.setOnClickListener {
                Log.d(TAG, "search open")
                recyclerSearch.isVisible = true
            }

            searchView.setOnSearchClickListener{
                Log.d(TAG, "search open")
                recyclerSearch.isVisible = true
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getAllLessons(database : FirebaseFirestore): ArrayList<LessonDetails> {
        var lessonsArrayList : ArrayList<LessonDetails> = arrayListOf<LessonDetails>()

        database.collection("lessons").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val building = document.data.getValue("building").toString()
                    val image = document.data.getValue("image").toString()
                    val lesson = document.data.getValue("lesson").toString()
                    val parking = document.data.getValue("parking").toString()
                    val professor = document.data.getValue("professor").toString()
                    val room = document.data.getValue("room").toString()

                    val lessonItem = LessonDetails(building, image, lesson, parking, professor, room)
                    lessonsArrayList.add(lessonItem)
                    loadingDialog.dismiss()
                    Log.d(TAG, "dismiss all lessons")
                }
                Log.d(TAG, "Rooms: $lessonsArrayList")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return lessonsArrayList
    }

    private fun initBubbleView(){
        navigationChoiceViewModel.bubbles.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { bubbleList ->
                if (bubbleList == null) return@Observer

                listBubbles.clear()
                listBubbles.addAll(bubbleList)
            })
//        var listBubbles = BubbleProvider().getAllBubblesLocal()

        bubbleAdapter = BubbleListAdapter(listBubbles)

        val recyclerBubbles = view?.findViewById<RecyclerView>(R.id.rv_bubbles)
        val bubblesLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerBubbles?.layoutManager = bubblesLayoutManager
        recyclerBubbles?.adapter = bubbleAdapter

        bubbleAdapter.bubbleSelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                bubbles -> if (bubbles == null) return@Observer
            bubblesSelected = bubbles
        })
    }

    private fun initLessonView(){
//        var mylessons = getAllLessons(FirebaseFirestore.getInstance())
        navigationChoiceViewModel.lessons.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { lessonsList ->
                if (lessonsList == null) return@Observer

                listLessons.clear()
                Log.d(TAG, "lesson list $lessonsList")
                listLessons.addAll(lessonsList)
                loadingDialog.dismiss()
            })

        lessonAdapter = LessonListAdapter(listLessons)
        recyclerLessons = view?.findViewById(R.id.rv_lessons)!!
        val lessonsLayoutManager = LinearLayoutManager(requireContext())
        recyclerLessons?.layoutManager = lessonsLayoutManager
        recyclerLessons?.adapter = lessonAdapter

        //capture lesson change
        lessonAdapter.lessonSelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer { lesson ->
            if (lesson == null) {
                lessonSelected= null
                return@Observer}
            lessonSelected = lesson

            setSelectedTimeDate(lessonSelected!!)
        })
    }

    private fun setSelectedTimeDate(lessonSelected: LessonListModel) {
        Log.d(TAG, "selected this ${lessonSelected.title} ${lessonSelected.lessonToString()}")
        // update times
        date.hour = lessonSelected.lessonTime.startDate.hours
        date.minutes = lessonSelected.lessonTime.startDate.minutes
        updateTimeText(date.hour, date.minutes)

        // update date
        val sdf = SimpleDateFormat("dd MMM")
        dateButton?.text = sdf.format(lessonSelected.lessonTime.startDate.time)

    }


    private fun initGoButton() {
        goButton = view?.findViewById(R.id.btn_go)
        goButton?.setOnClickListener {view ->
            if(lessonSelected != null){
                sendNavigationDetails(view)
            }
            else {
                Toast.makeText(
                    context,
                    "Please select a destination.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendNavigationDetails(view: View) {
        Log.d(TAG, "bubble list to send $bubblesSelected")
//        val selectedLocation = binding.navigationChoiceViewModel?.getSelectedLocation(selectedIndex)
        val navigationDetail = NavigationDetails(lessonSelected, date, bubblesSelected)
        val actionDetail = NavigationChoiceFragmentDirections.actionResult(navigationDetail)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun initTimePicker() {
        timeButton = view?.findViewById(R.id.btn_time)
        timeButton?.text = String.format("Now")
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

                Log.d(TAG, "rooms list $roomsList")
//                loadingDialog.dismiss()
            })
    }

    private fun openTimePicker(){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(date.hour)
            .setMinute(date.minutes)
            .setTitleText("Select Time")
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
            .setTitleText("Select Date")
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
