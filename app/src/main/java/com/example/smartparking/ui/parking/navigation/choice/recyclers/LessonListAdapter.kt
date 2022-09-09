package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R

internal class LessonListAdapter(private var lessonList: List<LessonListModel>):
        RecyclerView.Adapter<LessonListAdapter.MyViewHolder>(){

    private var _selectedLesson: MutableLiveData<LessonListModel> = MutableLiveData<LessonListModel>()

    internal inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title: TextView = view.findViewById(R.id.tv_lesson_name)
        var description: TextView = view.findViewById(R.id.tv_room_details)
        var dateTime: TextView = view.findViewById(R.id.tv_time_details)
        var lessonClickable: LinearLayout = view.findViewById(R.id.ll_lesson_clickable)
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
        holder.title.text = lesson.title
        holder.description.text = lesson.description
        holder.dateTime.text = lesson.dateTime

        holder.lessonClickable.setOnClickListener {
            lessonList[position].isSelected = !lessonList[position].isSelected
            _selectedLesson.value = lessonList[position]

            resetSelection(position)
            notifyDataSetChanged()
        }
        if(lessonList[position].isSelected){
            holder.lessonClickable.setBackgroundResource(R.drawable.lesson_selected)
        }
        else{
            holder.lessonClickable.setBackgroundResource(R.drawable.lesson)
        }
    }

    private fun resetSelection(position: Int) {
        for ((index, les) in lessonList.withIndex()) {
            if (index != position){
                les.isSelected = false
            }
        }
    }

    override fun getItemCount() = lessonList.count()

    internal var selectedLesson : MutableLiveData<LessonListModel>
        get() {return _selectedLesson }
        set(value) { _selectedLesson = value}

}