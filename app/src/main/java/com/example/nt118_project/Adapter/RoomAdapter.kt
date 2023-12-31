package com.example.nt118_project.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.example.nt118_project.hotel.ListofRoomsActivity
import java.text.DecimalFormat

class RoomAdapter(private var dataList: ArrayList<Room>, private var context: Context):RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    public var onItemClick:((Room) ->Unit)? = null
    inner class RoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvName: TextView = itemView.findViewById(R.id.tv_name_room)
        var tvMax: TextView = itemView.findViewById(R.id.tv_num)
        var tvService: TextView = itemView.findViewById(R.id.list_service)
        var tvPrice: TextView = itemView.findViewById(R.id.price)
        var layout :LinearLayout = itemView.findViewById(R.id.image_layout)
        var image1: ImageView = layout.findViewById(R.id.image1)
        var sublayout: LinearLayout = layout.findViewById(R.id.image_sublayout)
        var image2: ImageView = sublayout.findViewById(R.id.image2)
        var image3: ImageView = sublayout.findViewById(R.id.image3)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RoomAdapter.RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_rooms,parent,false)
        return RoomViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder:RoomAdapter.RoomViewHolder, position: Int) {
        val currItem: Room = dataList[position]
        holder.tvName.setText(currItem.Name)
        holder.tvName.isSelected = true
        holder.tvMax.setText(currItem.Max.toString()+" khách/phòng")
        holder.tvPrice.setText(formatter(currItem.Price) + " VND")
        holder.tvService.text = currItem.Service
        currItem.Img.add(currItem.Img[0])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
        Glide.with(context).load(currItem.Img[0])
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image1);
        Glide.with(context).load(currItem.Img[1])
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image2);
        Glide.with(context).load(currItem.Img[2])
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image3);
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
}