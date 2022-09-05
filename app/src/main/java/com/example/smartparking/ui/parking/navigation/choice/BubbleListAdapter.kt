package com.example.smartparking.ui.parking.navigation.choice

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartparking.R
import de.hdodenhof.circleimageview.CircleImageView

internal class BubbleListAdapter(private var bubblesList: List<BubbleListModel>) :
    RecyclerView.Adapter<BubbleListAdapter.MyViewHolder>() {

    private val selected: ArrayList<Int> = ArrayList()
    private lateinit var clickListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        clickListener = listener
    }

    internal inner class MyViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_bubble_name)
        var icon: ImageView = view.findViewById(R.id.bubble_icon)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.bubble_list, parent, false)

        return MyViewHolder(view, clickListener)
    }

    override fun getItemCount() = bubblesList.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bubble = bubblesList[position]
        holder.title.text = bubble.title
        holder.icon.setImageResource(bubble.icon)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(holder.icon.context, R.drawable.circle)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)

        val payload = !bubblesList[holder.adapterPosition].isSelected
        holder.itemView.setOnClickListener {
            if(payload){
                holder.icon.setBackgroundResource(R.drawable.circle_selected)

            }

            else{
                holder.icon.setBackgroundResource(R.drawable.circle)
            }

        }


    }
}
//
//class BubbleListAdapter(
//    var myContext: Context,
//    var resources: Int,
//    var bubbles: MutableList<BubbleListModel>
//):ArrayAdapter<BubbleListModel>(myContext,resources,bubbles) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(myContext)
//        val view: View = layoutInflater.inflate(resources, null)
//
//        val titleBubble: TextView = view.findViewById(R.id.tv_bubble_name)
//        val iconBubble: ImageView = view.findViewById(R.id.bubble_icon)
//
//        var bubbleItem: BubbleListModel = bubbles[position]
//        titleBubble.text = bubbleItem.title
//        iconBubble.setImageDrawable(myContext.resources.getDrawable(bubbleItem.icon))
//
//        return view
//    }
//}