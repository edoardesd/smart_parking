package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.data.db.SmartParkingApplication.Companion.globalUserType
import com.example.smartparking.internal.UserType
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat

internal class LessonListAdapter(private var lessonList: List<LessonListModel>) :
    RecyclerView.Adapter<LessonListAdapter.MyViewHolder>() {

    private var _lessonSelected: MutableLiveData<LessonListModel?> =
        MutableLiveData<LessonListModel?>()
    private var _recyclerView: RecyclerView? = null
    private var scrollPosition = 0


    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_lesson_name)
        var description: TextView = view.findViewById(R.id.tv_room_details)
        var prof: TextView = view.findViewById(R.id.tv_prof_name)
        var dateTime: TextView = view.findViewById(R.id.tv_time_details)
        var preview: ImageView = view.findViewById(R.id.iv_bubble_preview)
        var lessonClickable: LinearLayout = view.findViewById(R.id.ll_lesson_clickable)
        var expandedLayout: ConstraintLayout = view.findViewById(R.id.expanded_layout)
        var guestDetails: TextView = view.findViewById(R.id.tv_time_details)
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
            .inflate(R.layout.row_lesson, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lesson = lessonList[position]
        val sdfStart = SimpleDateFormat("EEEE HH:mm")
        val sdfEnd = SimpleDateFormat("HH:mm")
        holder.title.text = lesson.title
        holder.description.text = lesson.description
        holder.prof.text = lesson.prof
        if (globalUserType == UserType.GUEST) {
            holder.guestDetails.text = lesson.guestDetails
            holder.title.setTextColor(ContextCompat.getColor(holder.title.context ,R.color.mediumGray5G))
            holder.description.setTextColor(ContextCompat.getColor(holder.description.context ,R.color.mediumGray5G))
            holder.prof.setTextColor(ContextCompat.getColor(holder.prof.context ,R.color.mediumGray5G))
            holder.guestDetails.setTextColor(ContextCompat.getColor(holder.guestDetails.context ,R.color.mediumGray5G))
        } else {
            holder.dateTime.text =
                sdfStart.format(lesson.lessonTime.startDate) + "-" + sdfEnd.format(lesson.lessonTime.endDate)
        }
        holder.preview.setImageResource(lesson.preview)

        holder.expandedLayout.visibility = if (lesson.isSelected) View.VISIBLE else View.GONE

        holder.lessonClickable.setOnClickListener {
            lessonList[position].isSelected = !lesson.isSelected
            _lessonSelected.value = lessonList[position]

            scrollPosition = holder.adapterPosition
            resetSelection(position)
            notifyDataSetChanged()
        }

        if (lessonList[position].isSelected) {
            holder.lessonClickable.setBackgroundResource(R.drawable.lesson_selected)
            if (globalUserType == UserType.GUEST){
                holder.title.setTextColor(ContextCompat.getColor(holder.title.context ,R.color.white))
                holder.description.setTextColor(ContextCompat.getColor(holder.description.context ,R.color.white))
                holder.prof.setTextColor(ContextCompat.getColor(holder.prof.context ,R.color.white))
                holder.guestDetails.setTextColor(ContextCompat.getColor(holder.guestDetails.context ,R.color.white))
            }
            _recyclerView?.smoothScroll(position)
        } else {
            if (globalUserType == UserType.GUEST){
                holder.lessonClickable.setBackgroundResource(R.drawable.lesson_search)
                holder.title.setTextColor(ContextCompat.getColor(holder.title.context ,R.color.mediumGray5G))
                holder.description.setTextColor(ContextCompat.getColor(holder.description.context ,R.color.mediumGray5G))
                holder.prof.setTextColor(ContextCompat.getColor(holder.prof.context ,R.color.mediumGray5G))
                holder.guestDetails.setTextColor(ContextCompat.getColor(holder.guestDetails.context ,R.color.mediumGray5G))

            }else{
            holder.lessonClickable.setBackgroundResource(R.drawable.lesson)
            }
        }

        if (lessonList.all { !it.isSelected }) {
            _lessonSelected.value = null
            _recyclerView?.smoothScroll(scrollPosition)
        }
    }

    private fun RecyclerView.smoothScroll(
        toPos: Int,
        duration: Int = 200,
        onFinish: () -> Unit = {}
    ) {
        try {
            val smoothScroller: RecyclerView.SmoothScroller =
                object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_END
                    }

                    override fun calculateTimeForScrolling(dx: Int): Int {
                        return duration
                    }

                    override fun onStop() {
                        super.onStop()
                        onFinish.invoke()
                    }
                }
            smoothScroller.targetPosition = toPos
            layoutManager?.startSmoothScroll(smoothScroller)
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "FAILED TO SMOOTH SCROLL: ${e.message}")
        }
    }

    private fun resetSelection(position: Int) {
        for ((index, les) in lessonList.withIndex()) {
            if (index != position) {
                les.isSelected = false
            }
        }
    }

    override fun getItemCount() = lessonList.count()

    internal var lessonSelected: MutableLiveData<LessonListModel?>
        get() {
            return _lessonSelected
        }
        set(value) {
            _lessonSelected = value
        }
}