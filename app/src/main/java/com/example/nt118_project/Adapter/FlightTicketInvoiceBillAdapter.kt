package com.example.nt118_project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.BusTicketInvoice
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FlightTicketInvoiceBillAdapter(private var dataList: ArrayList<BusTicketInvoice>): RecyclerView.Adapter<FlightTicketInvoiceBillAdapter.FlightTicketInvoiceBillViewHolder>(){
    public var onItemClick: ((FlightTicket) -> Unit)? = null
    inner class FlightTicketInvoiceBillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVDate: TextView = itemView.findViewById<TextView>(R.id.tVDate)
        var tVFromTo: TextView = itemView.findViewById<TextView>(R.id.tVFromTo)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tVPrice)

        var tVName_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVName_ticket_2)
        var tVDate_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVDate_ticket_2)
        var tVFromTo_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVFromTo_ticket_2)

        var Frame_ticket_2: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.SecondTicket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightTicketInvoiceBillViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.busticket_invoice_item,parent,false)
        return FlightTicketInvoiceBillViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FlightTicketInvoiceBillViewHolder, position: Int) {
        val currItem: BusTicketInvoice = dataList[position]
        val databaseReference = Firebase.firestore
        databaseReference.collection("Flight").whereIn("Id", listOf(currItem.FirstBusTicket_ID, currItem.SecondBusTicket_ID)).get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    var flightticket_ = document.toObject(FlightTicket::class.java)
                    if(flightticket_.Id == currItem.FirstBusTicket_ID)
                    {
                        holder.tVName.setText(flightticket_.Name)
                        holder.tVDate.setText("Ngày " + flightticket_.Date + " " + flightticket_.DepartureTime + " - "+ flightticket_.ArrivalTime)
                        holder.tVFromTo.setText(flightticket_.From + " -> " + flightticket_.To)
                    }
                    else if(flightticket_.Id == currItem.SecondBusTicket_ID)
                    {
                        holder.tVName_ticket_2.setText(flightticket_.Name)
                        holder.tVDate_ticket_2.setText("Ngày " + flightticket_.Date + " " + flightticket_.DepartureTime + " - "+ flightticket_.ArrivalTime)
                        holder.tVFromTo_ticket_2.setText(flightticket_.From + " -> " + flightticket_.To)
                    }
                }
                if(currItem.SecondBusTicket_ID == "")
                {
                    holder.Frame_ticket_2.setVisibility(View.GONE)
                }
                holder.tVPrice.setText(currItem.Price + " VND ("+currItem.NumCus+" vé)")
            }
    }
}
