package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R

internal class SearchAdapter(private var lessonSearch: List<LessonListModel>):
        RecyclerView.Adapter<SearchAdapter.MyViewHolder>(){

    private var _searchSelected: MutableLiveData<LessonListModel> =  MutableLiveData<LessonListModel>()
    private var _recyclerView: RecyclerView? = null
//    private var scrollPosition = 0


    internal inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title: TextView = view.findViewById(R.id.search_lesson_name)
        var description: TextView = view.findViewById(R.id.search_room_details)
        var roomDetails: TextView = view.findViewById(R.id.search_time_details)
        var roomType: TextView = view.findViewById(R.id.search_room_type)
        var searchClickable: LinearLayout = view.findViewById(R.id.ll_search_clickable)
//        var expandedLayout: ConstraintLayout = view.findViewById(R.id.expanded_layout)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        _recyclerView = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_search, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lesson = lessonSearch[position]
        holder.title.text = lesson.title
        holder.description.text = lesson.description
        holder.roomType.text = lesson.prof
        holder.roomDetails.text = lesson.guestDetails

        holder.searchClickable.setOnClickListener {
            lessonSearch[position].isSelected = true

            Log.d(TAG, "selected $lessonSearch")
            _searchSelected.value = lessonSearch[position]
        }
    }

    override fun getItemCount() = lessonSearch.count()

    internal var searchSelected : MutableLiveData<LessonListModel>
        get() {return _searchSelected }
        set(value) { _searchSelected = value}
}