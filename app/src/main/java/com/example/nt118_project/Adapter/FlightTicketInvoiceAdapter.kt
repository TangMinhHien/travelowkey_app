package com.example.nt118_project.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Fragments.FillUserInfoFragment
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.R
import java.text.DecimalFormat
import com.bumptech.glide.Glide

class FlightTicketInvoiceAdapter(private var dataList: ArrayList<FlightTicket>, private var context: Context): RecyclerView.Adapter<FlightTicketInvoiceAdapter.FlightTicketViewHolder>() {

    public var onItemClick: ((FlightTicket) -> Unit)? = null
    inner class FlightTicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.airplane)
        var tVDepartureTime: TextView = itemView.findViewById<TextView>(R.id.departureTime)
        var tVArrivalTime: TextView = itemView.findViewById<TextView>(R.id.arrivalTime)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.price)
        var tVFrom: TextView = itemView.findViewById<TextView>(R.id.from)
        var tVTo: TextView = itemView.findViewById<TextView>(R.id.to)
        var tVStop_direct:TextView = itemView.findViewById<TextView>(R.id.stop_direct)
        var tVTravelTime:TextView = itemView.findViewById<TextView>(R.id.travelTime)
        var tVSelectedBtn: TextView = itemView.findViewById<TextView>(R.id.select_btn)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.logo)
        val dictionary = hashMapOf("Bamboo Airways" to "https://e7.pngegg.com/pngimages/322/70/png-clipart-logo-brand-graphic-design-bamboo-airways-graphics-transport-bamboo-text-logo.png"
            , "VietJet Air" to "https://logosdownload.com/logo/vietjet-air-logo-1024.png"
            ,"Vietnam Airlines" to "https://th.bing.com/th/id/R.69568b3f87339dd9f27b6f8b55e27f37?rik=aQZvi%2fPzh3TwWw&riu=http%3a%2f%2f3.bp.blogspot.com%2f-RKyj5KiLuoU%2fUWdoTdCtIHI%2fAAAAAAAAA_8%2f8aw9S9h1BLI%2fs1600%2fVietnam%2bAirlines.jpg&ehk=rsjhjLnyYt8QfTTO4GX2aqFx2GuUEGYBMtiUuPbktXk%3d&risl=&pid=ImgRaw&r=0"
            ,"Vietravel Airlines" to "https://airhex.com/images/airline-logos/alt/vietravel-airlines.png")
        val set = setOf("Bamboo Airways","VietJet Air","Vietnam Airlines","Vietravel Airlines")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightTicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_flights,parent,false)
        return FlightTicketViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FlightTicketViewHolder, position: Int) {
        val currItem: FlightTicket = dataList[position]
        holder.tVName.setText(currItem.Name)
        holder.tVDepartureTime.setText(currItem.DepartureTime)
        holder.tVArrivalTime.setText(currItem.ArrivalTime)
        holder.tVFrom.setText(currItem.From)
        holder.tVTo.setText(currItem.To)
        holder.tVTravelTime.setText(currItem.TravelTime)
        holder.tVStop_direct.setText(currItem.Stop_Direct)
        holder.tVPrice.setText(formatter(currItem.Price) + " VND/khách")
        holder.tVSelectedBtn.setVisibility(View.GONE)
        var value: String = holder.dictionary["VietJet Air"]!!
        if (holder.set.contains(currItem.Name)){
            value = holder.dictionary[currItem.Name]!!
        }
        Glide.with(context).load(value)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image);
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
}