package com.example.nt118_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nt118_project.Model.Notification
import com.example.nt118_project.R

class NotificationAdapter(private var dataList: ArrayList<Notification>, private var context: Context): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    public var onItemClick:((Notification) ->Unit)? = null

    inner class NotificationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var text :TextView = itemView.findViewById(R.id.text)
        var seen: ImageView = itemView.findViewById(R.id.ic_seen)
        var unseen: ImageView = itemView.findViewById(R.id.ic_unseen)
        var context: View = itemView

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_notification,parent,false)
        return NotificationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currItem: Notification = dataList[position]
        holder.text.setText(currItem.Text)
        val myColor = ContextCompat.getColor(context, R.color.black)
        if (currItem.State == "Seen"){
            holder.context.backgroundTintList = ContextCompat.getColorStateList(context,R.color.black)
            holder.unseen.visibility = View.GONE
            holder.seen.visibility = View.VISIBLE
        }
        else{
            holder.seen.visibility = View.GONE
            holder.unseen.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }
}