package com.example.nt118_project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class BusTicketPayAdapter(private var dataList: ArrayList<BusTicket>): RecyclerView.Adapter<BusTicketPayAdapter.BusTicketViewHolder>() {

    public var onItemClick: ((BusTicket) -> Unit)? = null
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    inner class BusTicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVNumberOfSeat: TextView = itemView.findViewById<TextView>(R.id.tVNumberOfSeat)
        var tVDepartureTime: TextView = itemView.findViewById<TextView>(R.id.tVDepartureTime)
        var tVArrivalTime: TextView = itemView.findViewById<TextView>(R.id.tVArrivalTime)
        var tVTravelTime: TextView = itemView.findViewById<TextView>(R.id.tVTravelTime)
        var tVMoney: TextView = itemView.findViewById<TextView>(R.id.tVMoney)
        var tVDeparturePoint: TextView = itemView.findViewById<TextView>(R.id.tVDeparturePoint)
        var tVArrivalPoint: TextView = itemView.findViewById<TextView>(R.id.tVArrivalPoint)
        var tVDate: TextView = itemView.findViewById(R.id.tVDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusTicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.busticket_pay_item,parent,false)
        return BusTicketViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BusTicketViewHolder, position: Int) {
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val currItem:BusTicket = dataList[position]
        holder.tVName.setText(currItem.Name)
        holder.tVNumberOfSeat.setText(currItem.Type)
        val date = inputFormat.parse(currItem.Date)
        val Date= "Ngày đi: " + outputFormat.format(date!!)
        holder.tVDate.setText(Date)
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
