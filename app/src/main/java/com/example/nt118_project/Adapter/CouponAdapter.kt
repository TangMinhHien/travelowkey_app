package com.example.nt118_project.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.Coupon
import com.example.nt118_project.Model.Point
import com.example.nt118_project.R

class CouponAdapter (private var dataList: ArrayList<Coupon>, private var context: Context, private var isUsed:Boolean): RecyclerView.Adapter<CouponAdapter.CouponAdapterViewHolder>() {
    public var onItemClick:((Coupon) ->Unit)? = null

    inner class CouponAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nameCoupon : TextView = itemView.findViewById(R.id.tVNameCoupon)
        var tVContent: TextView = itemView.findViewById(R.id.tVContent)
        var tVCode: TextView = itemView.findViewById(R.id.tVCode)
        var tVExpiryDate: TextView = itemView.findViewById(R.id.tVExpiryDate)
        var layout:ConstraintLayout = itemView.findViewById(R.id.layout)
        var context: View = itemView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_coupon,parent,false)
        return CouponAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CouponAdapterViewHolder, position: Int) {
        val currItem: Coupon = dataList[position]
        holder.tVContent.setText(currItem.Text)
        holder.tVCode.setText("Code: "+currItem.Code)
        holder.tVExpiryDate.setText("Ngày hết hạn: "+currItem.ExpiryDate)
        if(currItem.Tag == "Bus")
            holder.nameCoupon.setText("Khuyến mãi dành cho đặt vé xe khách")
        if(currItem.Tag == "Flight")
            holder.nameCoupon.setText("Khuyến mãi dành cho đặt vé máy bay")
        if(currItem.Tag == "Hotel")
            holder.nameCoupon.setText("Khuyến mãi dành cho đặt phòng khách sạn")
        if(currItem.Tag == "ServiceCar")
            holder.nameCoupon.setText("Khuyến mãi dành cho đặt xe dịch vụ")
        if(isUsed == true)
        {
            holder.layout.setBackgroundColor(Color.WHITE)
            val temp_colors = intArrayOf(
                Color.WHITE,
            )
            val states = arrayOf(
                intArrayOf(android.R.attr.state_enabled), // enabled
            )
            val colors = ColorStateList(states,temp_colors)
            holder.layout.backgroundTintList = colors
        }
        val myColor = ContextCompat.getColor(context, R.color.black)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }
}