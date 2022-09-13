package com.example.smartparking.ui.parking.navigation.choice.recyclers

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R

internal class BubbleListAdapter(private var bubblesList: List<BubbleListModel>) :
    RecyclerView.Adapter<BubbleListAdapter.MyViewHolder>() {

    private var _bubbleSelected: MutableLiveData<ArrayList<BubbleListModel>> = MutableLiveData<ArrayList<BubbleListModel>>(arrayListOf())

    internal inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_bubble_name)
        var icon: ImageView = view.findViewById(R.id.bubble_icon)
        var bubbleClickable: RelativeLayout = view.findViewById(R.id.rl_bubble_clickable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.bubble_list, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount() = bubblesList.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bubble = bubblesList[position]
        holder.title.text = bubble.title
        holder.icon.setImageResource(bubble.icon)

        holder.bubbleClickable.setOnClickListener {
            bubblesList[position].isSelected = !bubblesList[position].isSelected
            if(bubblesList[position].isSelected){
                holder.icon.setBackgroundResource(R.drawable.circle_selected)
                _bubbleSelected.value?.add(bubble)
            }
            else{
                holder.icon.setBackgroundResource(R.drawable.circle)
                _bubbleSelected.value?.remove(bubble)
            }
        }
    }

    internal var bubbleSelected : MutableLiveData<ArrayList<BubbleListModel>>
        get() {return _bubbleSelected}
        set(value) {_bubbleSelected = value}
}