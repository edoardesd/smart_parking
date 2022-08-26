package com.example.smartparking.ui.parking.navigation.choice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R

class BubbleListAdapter(val bubbles: ArrayList<String>) :
    RecyclerView.Adapter<BubbleListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.bubble_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bubbleName.text = bubbles[position]
    }

    override fun getItemCount() = bubbles.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bubbleName: TextView = itemView.findViewById(R.id.tv_bubble_name)
    }




}
