package com.example.smartparking.ui.parking.navigation.result.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import com.example.smartparking.internal.CrowdLevel
import com.example.smartparking.ui.parking.navigation.choice.recyclers.BubbleListModel

internal class BubbleSelectedAdapter(private var bubblesList: List<BubbleListModel>) :
    RecyclerView.Adapter<BubbleSelectedAdapter.MyViewHolder>() {

    private val selected: ArrayList<Int> = ArrayList()

    internal inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_bubble_selected_name)
        var icon: ImageView = view.findViewById(R.id.bubble_icon_selected)
        var crowd: TextView = view.findViewById(R.id.tv_bubble_crowd_selected)
        var minutes: TextView = view.findViewById(R.id.tv_bubble_minutes)
        var friends: TextView = view.findViewById(R.id.tv_bubble_friends)
//        var bubbleClickable: RelativeLayout = view.findViewById(R.id.rl_bubble_clickable)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.bubble_selected, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount() = bubblesList.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bubble = bubblesList[position]
        holder.title.text = bubble.title
        holder.icon.setImageResource(bubble.icon)
        holder.crowd.text = bubble.crowdLevel.name
        holder.minutes.text = "+ ${bubble.extraMinutes.inWholeMinutes} min"
        holder.friends.text = "${getNumberOfFriends(bubble)} bubble friends are there!"
    }

    private fun getNumberOfFriends(bubble: BubbleListModel): Int{
        val numFriends = when(bubble.crowdLevel){
            CrowdLevel.QUIET -> (0..5).random()
            CrowdLevel.BUSY -> (6..12).random()
            CrowdLevel.CROWDED -> (8..25).random()
        }
        return numFriends
    }
}