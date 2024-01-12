package com.example.nt118_project.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.Point
import com.example.nt118_project.R

class AccumulatedPointsAdapter (private var dataList: ArrayList<Point>, private var context: Context): RecyclerView.Adapter<AccumulatedPointsAdapter.AccumulatedPointsViewHolder>() {
    public var onItemClick:((Point) ->Unit)? = null

    inner class AccumulatedPointsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var text : TextView = itemView.findViewById(R.id.text)
        var tVpoint: TextView = itemView.findViewById(R.id.textPoint)
        var context: View = itemView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccumulatedPointsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_point,parent,false)
        return AccumulatedPointsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AccumulatedPointsViewHolder, position: Int) {
        val currItem: Point = dataList[position]
        holder.text.setText(currItem.Text)
        val myColor = ContextCompat.getColor(context, R.color.black)
        holder.tVpoint.text = currItem.points + " điểm"
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }
}