package com.example.nt118_project.Adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.BusTicketInvoice
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.DecimalFormat
import com.bumptech.glide.Glide

class FlightTicketInvoiceBillAdapter(private var dataList: ArrayList<BusTicketInvoice>, private var mcontext: Context): RecyclerView.Adapter<FlightTicketInvoiceBillAdapter.FlightTicketInvoiceBillViewHolder>(){
    public var onItemClick: ((FlightTicket) -> Unit)? = null
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    inner class FlightTicketInvoiceBillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.tVName)
        var tVDate: TextView = itemView.findViewById<TextView>(R.id.tVDate)
        var tVFromTo: TextView = itemView.findViewById<TextView>(R.id.tVFromTo)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tVPrice)

        var tVName_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVName_ticket_2)
        var tVDate_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVDate_ticket_2)
        var tVFromTo_ticket_2: TextView = itemView.findViewById<TextView>(R.id.tVFromTo_ticket_2)

        var Frame_ticket_2: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.SecondTicket)
        var Line_view: View = itemView.findViewById<View>(R.id.line)

        val dictionary = hashMapOf("Bamboo Airways" to "https://e7.pngegg.com/pngimages/322/70/png-clipart-logo-brand-graphic-design-bamboo-airways-graphics-transport-bamboo-text-logo.png"
            , "VietJet Air" to "https://logosdownload.com/logo/vietjet-air-logo-1024.png"
            ,"Vietnam Airlines" to "https://th.bing.com/th/id/R.69568b3f87339dd9f27b6f8b55e27f37?rik=aQZvi%2fPzh3TwWw&riu=http%3a%2f%2f3.bp.blogspot.com%2f-RKyj5KiLuoU%2fUWdoTdCtIHI%2fAAAAAAAAA_8%2f8aw9S9h1BLI%2fs1600%2fVietnam%2bAirlines.jpg&ehk=rsjhjLnyYt8QfTTO4GX2aqFx2GuUEGYBMtiUuPbktXk%3d&risl=&pid=ImgRaw&r=0"
            ,"Vietravel Airlines" to "https://airhex.com/images/airline-logos/alt/vietravel-airlines.png")
        val set = setOf("Bamboo Airways","VietJet Air","Vietnam Airlines","Vietravel Airlines")
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
                for (document in documents)
                {
                    var flightticket_ = document.toObject(FlightTicket::class.java)
                    if(flightticket_.Id == currItem.FirstBusTicket_ID)
                    {
                        price_ticket_1 = this.formatter(flightticket_.Price) + " VND/vé"
                        Date_1= "Ngày đi: " + flightticket_.Date
                        FromTo_ticket_1 = flightticket_.From + " --> " + flightticket_.To
                        Type_ticket_1 = "Hạng vé: " + flightticket_.SeatClass
                        TravelTime_ticket_1 = flightticket_.DepartureTime + " --> " + flightticket_.ArrivalTime + " ("+flightticket_.TravelTime+")"
                        holder.tVName.setText(flightticket_.Name)
                        holder.tVDate.setText("Ngày đi: " + flightticket_.Date + " " + flightticket_.DepartureTime + " - "+ flightticket_.ArrivalTime)
                        holder.tVFromTo.setText(flightticket_.From + " -> " + flightticket_.To)
                    }
                    else if(flightticket_.Id == currItem.SecondBusTicket_ID)
                    {
                        price_ticket_2 = this.formatter(flightticket_.Price) + " VND/vé"
                        Date_2= "Ngày đi: " + flightticket_.Date
                        FromTo_ticket_2 = flightticket_.From + " --> " + flightticket_.To
                        Type_ticket_2 = "Hạng vé: " + flightticket_.SeatClass
                        TravelTime_ticket_2 = flightticket_.DepartureTime + " --> " + flightticket_.ArrivalTime + " ("+flightticket_.TravelTime+")"
                        holder.tVName_ticket_2.setText(flightticket_.Name)
                        holder.tVDate_ticket_2.setText("Ngày về: " + flightticket_.Date + " " + flightticket_.DepartureTime + " - "+ flightticket_.ArrivalTime)
                        holder.tVFromTo_ticket_2.setText(flightticket_.From + " -> " + flightticket_.To)
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
                    var value: String = holder.dictionary["VietJet Air"]!!
                    if (holder.set.contains(tVName_di.text)){
                        value = holder.dictionary[tVName_di.text]!!
                    }
                    Glide.with(mcontext).load(value)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(logo_1);
                    if(currItem.SecondBusTicket_ID == "")
                    {
                        Frame_ticket_2_di.setVisibility(View.GONE)
                        line.setVisibility(View.GONE)
                    }
                    else
                    {
                        var value: String = holder.dictionary["VietJet Air"]!!
                        if (holder.set.contains(tVName_ticket_2_di.text)){
                            value = holder.dictionary[tVName_ticket_2_di.text]!!
                        }
                        Glide.with(mcontext).load(value)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(logo_2);
                    }
                    builder.setContentView(view)
                    builder.show()
                }
            }
    }
}
