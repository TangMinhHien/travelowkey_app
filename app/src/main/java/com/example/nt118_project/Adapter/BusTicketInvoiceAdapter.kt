package com.example.nt118_project.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.BusTicketInvoice
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class BusTicketInvoiceAdapter (private var dataList: ArrayList<BusTicketInvoice>, private var mcontext:Context): RecyclerView.Adapter<BusTicketInvoiceAdapter.BusTicketInvoiceViewHolder>() {
    public var onItemClick: ((BusTicketInvoice) -> Unit)? = null
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    inner class BusTicketInvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVDate: TextView = itemView.findViewById<TextView>(R.id.tVDate)
        var tVFromTo: TextView = itemView.findViewById<TextView>(R.id.tVFromTo)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tVPrice)

        var tVName_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVName_ticket_2)
        var tVDate_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVDate_ticket_2)
        var tVFromTo_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVFromTo_ticket_2)

        var Frame_ticket_2:RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.SecondTicket)
        var Line_view: View = itemView.findViewById<View>(R.id.line)
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
                var price_ticket_1: String = ""
                var FromTo_ticket_1: String = ""
                var Type_ticket_1: String = ""
                var TravelTime_ticket_1: String = ""
                var price_ticket_2:String = ""
                var FromTo_ticket_2: String = ""
                var Type_ticket_2: String = ""
                var TravelTime_ticket_2: String = ""
                var Date_1: String = ""
                var Date_2: String = ""
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                for (document in documents)
                {
                    var busticket_ = document.toObject(BusTicket::class.java)
                    if(busticket_.Id == currItem.FirstBusTicket_ID)
                    {
                        price_ticket_1 = this.formatter(busticket_.Price) + " VND/vé"
                        val date = inputFormat.parse(busticket_.Date)
                        Date_1= "Ngày đi: " + outputFormat.format(date!!)
                        FromTo_ticket_1 = busticket_.From + " ("+ busticket_.PickPoint + ") --> " + busticket_.To + " (" + busticket_.DesPoint + ")"
                        Type_ticket_1 = busticket_.Type
                        TravelTime_ticket_1 = busticket_.DepartureTime + " --> " + busticket_.ArrivalTime + " ("+busticket_.TravelTime+")"
                        holder.tVName.setText(busticket_.Name)
                        holder.tVDate.setText(Date_1 + " " + busticket_.DepartureTime + " - "+ busticket_.ArrivalTime)
                        holder.tVFromTo.setText(busticket_.From + " -> " + busticket_.To)
                    }
                    else if(busticket_.Id == currItem.SecondBusTicket_ID)
                    {
                        price_ticket_2 = this.formatter(busticket_.Price) + " VND/vé"
                        val date = inputFormat.parse(busticket_.Date)
                        Date_2= "Ngày về: " + outputFormat.format(date!!)
                        FromTo_ticket_2 = busticket_.From + " ("+ busticket_.PickPoint + ") --> " + busticket_.To + " (" + busticket_.DesPoint + ")"
                        Type_ticket_2 = busticket_.Type
                        TravelTime_ticket_2 = busticket_.DepartureTime + " --> " + busticket_.ArrivalTime + " ("+busticket_.TravelTime+")"
                        holder.tVName_ticket_2.setText(busticket_.Name)
                        holder.tVDate_ticket_2.setText(Date_2 + " " + busticket_.DepartureTime + " - "+ busticket_.ArrivalTime)
                        holder.tVFromTo_ticket_2.setText(busticket_.From + " -> " + busticket_.To)
                    }
                }
                if(currItem.SecondBusTicket_ID == "")
                {
                    holder.Frame_ticket_2.setVisibility(View.GONE)
                    holder.Line_view.setVisibility(View.GONE)
                }
                holder.tVPrice.setText(this.formatter(currItem.Price.toDouble().toInt()).toString() + " VND ("+currItem.NumCus+" vé)")
                holder.itemView.setOnClickListener {
                    val builder = Dialog(mcontext)
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    builder.setTitle("Chi tiết hóa đơn")
                    builder.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
                    builder.setTitle("Chi tiết hóa đơn")
                    val view: View =
                        LayoutInflater.from(mcontext).inflate(R.layout.bus_ticket_dialog, null)
                    var tVName_di: TextView = view.findViewById<TextView>(R.id.tVName)
                    var tVDate_di: TextView = view.findViewById<TextView>(R.id.tVDate)
                    var tVFromTo_di: TextView = view.findViewById<TextView>(R.id.tVFromTo)
                    var tVPrice_di: TextView = view.findViewById<TextView>(R.id.tVPrice)
                    var tVType_1: TextView = view.findViewById<TextView>(R.id.tVType)
                    var tVPrice_1: TextView = view.findViewById(R.id.tVPrice_ticket_1)
                    var tVTravelTime_1: TextView = view.findViewById(R.id.tVTravelTime)
                    var logo_1: ImageView = view.findViewById(R.id.logo_1)
                    var logo_2: ImageView = view.findViewById(R.id.logo_2)

                    var line: View = view.findViewById<View>(R.id.line)

                    var tVName_ticket_2_di: TextView = view.findViewById<TextView>(R.id.tVName_ticket_2)
                    var tVDate_ticket_2_di: TextView = view.findViewById<TextView>(R.id.tVDate_ticket_2)
                    var tVFromTo_ticket_2_di: TextView = view.findViewById<TextView>(R.id.tVFromTo_ticket_2)
                    var tVType_2: TextView = view.findViewById<TextView>(R.id.tVType_2)
                    var tVPrice_2: TextView = view.findViewById(R.id.tVPrice_ticket_2)
                    var tVTravelTime_2: TextView = view.findViewById(R.id.tVTravelTime_2)

                    var Frame_ticket_2_di:RelativeLayout = view.findViewById<RelativeLayout>(R.id.SecondTicket)
                    logo_1.setVisibility(View.GONE)
                    logo_2.setVisibility(View.GONE)
                    tVName_di.setText(holder.tVName.text)
                    tVType_1.setText(Type_ticket_1)
                    tVDate_di.setText(Date_1)
                    tVFromTo_di.setText(FromTo_ticket_1)
                    tVPrice_1.setText(price_ticket_1)
                    tVTravelTime_1.setText(TravelTime_ticket_1)
                    tVPrice_di.setText(holder.tVPrice.text)

                    tVName_ticket_2_di.setText(holder.tVName.text)
                    tVType_2.setText(Type_ticket_2)
                    tVDate_ticket_2_di.setText(Date_2)
                    tVFromTo_ticket_2_di.setText(FromTo_ticket_2)
                    tVPrice_2.setText(price_ticket_2)
                    tVTravelTime_2.setText(TravelTime_ticket_2)
                    if(currItem.SecondBusTicket_ID == "")
                    {
                        Frame_ticket_2_di.setVisibility(View.GONE)
                        line.setVisibility(View.GONE)
                    }
                    builder.setContentView(view)
                    builder.show()
                }
            }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currItem)
        }
    }

}