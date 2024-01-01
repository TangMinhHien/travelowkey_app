package com.example.nt118_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.R
import java.text.DecimalFormat

class RecentHotelAdapter (private var dataList: ArrayList<Hotel>, private var context: Context): RecyclerView.Adapter<RecentHotelAdapter.RecentHotelViewHolder>(){
    public var onItemClick: ((Hotel) -> Unit)? = null
    inner class RecentHotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tv_name_hotel)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tv_price)
        var Ratingbar: RatingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.img_hotel)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecentHotelAdapter.RecentHotelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recent_hotels_search,parent,false)
        return RecentHotelViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecentHotelAdapter.RecentHotelViewHolder, position: Int) {
        val currItem: Hotel = dataList[position]

        holder.tVName.setText(currItem.Name)
        holder.tVName.isSelected = true
        holder.tVPrice.setText(formatter(currItem.Price) + " VND")
        holder.Ratingbar.numStars = currItem.Rating
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
        Glide.with(context).load(currItem.Img)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image);
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
}