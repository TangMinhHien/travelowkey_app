package com.example.nt118_project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import java.text.DecimalFormat

fun formatter(n: Int) =
    DecimalFormat("#,###")
        .format(n)
        .replace(",", ".")
class BusTicketAdapter(private var dataList: ArrayList<BusTicket>): RecyclerView.Adapter<BusTicketAdapter.BusTicketViewHolder>() {

    public var onItemClick: ((BusTicket) -> Unit)? = null
    inner class BusTicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVNumberOfSeat: TextView = itemView.findViewById<TextView>(R.id.tVNumberOfSeat)
        var tVDepartureTime: TextView = itemView.findViewById<TextView>(R.id.tVDepartureTime)
        var tVArrivalTime: TextView = itemView.findViewById<TextView>(R.id.tVArrivalTime)
        var tVTravelTime: TextView = itemView.findViewById<TextView>(R.id.tVTravelTime)
        var tVMoney: TextView = itemView.findViewById<TextView>(R.id.tVMoney)
        var tVDeparturePoint: TextView = itemView.findViewById<TextView>(R.id.tVDeparturePoint)
        var tVArrivalPoint: TextView = itemView.findViewById<TextView>(R.id.tVArrivalPoint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusTicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.busticket_item,parent,false)
        return BusTicketViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BusTicketViewHolder, position: Int) {
        val currItem:BusTicket = dataList[position]
        holder.tVName.setText(currItem.Name)
        holder.tVNumberOfSeat.setText(currItem.Type)
        holder.tVDepartureTime.setText(currItem.DepartureTime)
        holder.tVArrivalTime.setText(currItem.ArrivalTime)
        holder.tVTravelTime.setText(currItem.TravelTime)
        holder.tVMoney.setText(formatter(currItem.Price).toString() + " VND/chỗ")
        holder.tVDeparturePoint.setText(currItem.PickPoint)
        holder.tVArrivalPoint.setText(currItem.DesPoint)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }

    fun getDataList():ArrayList<BusTicket>
    {
        return dataList
    }

}
