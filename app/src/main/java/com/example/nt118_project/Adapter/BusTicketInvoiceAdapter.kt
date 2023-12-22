package com.example.nt118_project.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.BusTicketInvoice
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class BusTicketInvoiceAdapter (private var dataList: ArrayList<BusTicketInvoice>): RecyclerView.Adapter<BusTicketInvoiceAdapter.BusTicketInvoiceViewHolder>() {
    public var onItemClick: ((BusTicket) -> Unit)? = null
    inner class BusTicketInvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVDate: TextView = itemView.findViewById<TextView>(R.id.tVDate)
        var tVFromTo: TextView = itemView.findViewById<TextView>(R.id.tVFromTo)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tVPrice)

        var tVName_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVName_ticket_2)
        var tVDate_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVDate_ticket_2)
        var tVFromTo_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVFromTo_ticket_2)

        var Frame_ticket_2:RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.SecondTicket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusTicketInvoiceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.busticket_invoice_item,parent,false)
        return BusTicketInvoiceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BusTicketInvoiceViewHolder, position: Int) {
        val currItem: BusTicketInvoice = dataList[position]
        val databaseReference = Firebase.firestore
        databaseReference.collection("Bus").whereIn("Id", listOf(currItem.FirstBusTicket_ID, currItem.SecondBusTicket_ID)).get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    var busticket_ = document.toObject(BusTicket::class.java)
                    if(busticket_.Id == currItem.FirstBusTicket_ID)
                    {
                        holder.tVName.setText(busticket_.Name)
                        holder.tVDate.setText("Ngày " + busticket_.Date + " " + busticket_.DepartureTime + " - "+ busticket_.ArrivalTime)
                        holder.tVFromTo.setText(busticket_.From + " -> " + busticket_.To)
                    }
                    else if(busticket_.Id == currItem.SecondBusTicket_ID)
                    {
                        holder.tVName_ticket_2.setText(busticket_.Name)
                        holder.tVDate_ticket_2.setText("Ngày " + busticket_.Date + " " + busticket_.DepartureTime + " - "+ busticket_.ArrivalTime)
                        holder.tVFromTo_ticket_2.setText(busticket_.From + " -> " + busticket_.To)
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