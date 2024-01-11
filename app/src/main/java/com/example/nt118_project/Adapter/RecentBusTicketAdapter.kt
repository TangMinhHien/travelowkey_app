package com.example.nt118_project.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.R
import java.text.DecimalFormat
class RecentBusTicketAdapter (private var dataList: ArrayList<BusTicket>, private var context: Context): RecyclerView.Adapter<RecentBusTicketAdapter.RecentBusTicketAdapterViewHolder>() {

    public var onItemClick: ((BusTicket) -> Unit)? = null
    inner class RecentBusTicketAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDay: TextView = itemView.findViewById(R.id.date)
        var tVName: TextView = itemView.findViewById<TextView>(R.id.airplane)
        var tVDepartureTime: TextView = itemView.findViewById<TextView>(R.id.departureTime)
        var tVArrivalTime: TextView = itemView.findViewById<TextView>(R.id.arrivalTime)
        var tVFrom: TextView = itemView.findViewById<TextView>(R.id.from)
        var tVTo: TextView = itemView.findViewById<TextView>(R.id.to)
        var tVStop_direct: TextView = itemView.findViewById<TextView>(R.id.stop_direct)
        var tVTravelTime: TextView = itemView.findViewById<TextView>(R.id.travelTime)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBusTicketAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recent_flights_search,parent,false)
        Log.d("check_adapter","222222222222222222222222")
        return RecentBusTicketAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecentBusTicketAdapterViewHolder, position: Int) {
        Log.d("check_adapter","11111111111111111111111111")
        val currItem: BusTicket = dataList[position]
        holder.tVName.setText(currItem.Name)
        holder.tVDepartureTime.setText(currItem.DepartureTime)
        holder.tVArrivalTime.setText(currItem.ArrivalTime)
        holder.tVFrom.setText(currItem.From)
        holder.tVTo.setText(currItem.To)
        holder.tVTravelTime.setText(currItem.TravelTime)
        holder.tVStop_direct.setVisibility(View.GONE)
        holder.tvDay.setText(currItem.Date)
        holder.image.setVisibility(View.GONE)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }
}