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

class HotelSearchResultAdapter(private var dataList: ArrayList<Hotel>, private var context: Context): RecyclerView.Adapter<HotelSearchResultAdapter.HotelSearchResultViewHolder>(){
    public var onItemClick: ((Hotel) -> Unit)? = null
    inner class HotelSearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tv_name_hotel)
        var tvAddress: TextView = itemView.findViewById<TextView>(R.id.tv_address)
        var Ratingbar: RatingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.image)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):HotelSearchResultAdapter.HotelSearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_hotels_search_result,parent,false)
        return HotelSearchResultViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HotelSearchResultViewHolder, position: Int) {
        val currItem: Hotel = dataList[position]

        holder.tVName.setText(currItem.Name)
        holder.tVName.isSelected = true
        holder.tvAddress.setText((currItem.Address))
        holder.tvAddress.isSelected = true
        holder.Ratingbar.numStars = currItem.Rating
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
        Glide.with(context).load(currItem.Img)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image);
//        "https://cf.bstatic.com/xdata/images/hotel/max500/480774075.jpg?k=42cf5cfdbee9526aea18774a7f299ea833f0dec07d93ac72d8873b6a5f3cf0dc&amp;o=&amp;hp=1"
    }
}