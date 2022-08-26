package com.example.smartparking.ui.parking.navigation.choice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.smartparking.R

class LessonListAdapter (var myContext:Context,
                         var resources:Int,
                         var items:List<LessonListModel>):ArrayAdapter<LessonListModel>(myContext,
                                                                                        resources,
                                                                                        items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(myContext)
        val view:View = layoutInflater.inflate(resources, null)

        val titleTextView:TextView = view.findViewById(R.id.tv_lesson_name)
        val descriptionTextView:TextView = view.findViewById(R.id.tv_room_details)
        val dateTimeTextView:TextView = view.findViewById(R.id.tv_time_details)

        var lessonItem:LessonListModel = items[position]

        titleTextView.text = lessonItem.title
        descriptionTextView.text = lessonItem.description
        dateTimeTextView.text = lessonItem.dateTime

        return view
    }
}