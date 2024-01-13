package com.example.nt118_project.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nt118_project.Fragments.BillFragment
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.HotelTicketInvoice
import com.example.nt118_project.Model.Room
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.DecimalFormat

class CarServiceBillAdapter(private var dataList: ArrayList<BillFragment.CarSerVice_Bill>, private var mcontext: Context): RecyclerView.Adapter<CarServiceBillAdapter.CarServiceBillAdapterViewHolder>() {
    public var onItemClick: ((BillFragment.CarSerVice_Bill) -> Unit)? = null
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    inner class CarServiceBillAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.service_car)
        var tVNumSeat: TextView = itemView.findViewById<TextView>(R.id.seat_amount)
        var tVNumLuggage: TextView = itemView.findViewById<TextView>(R.id.luggage_amount)
        var tVCompany: TextView = itemView.findViewById<TextView>(R.id.supplier)
        var tVPlace: TextView = itemView.findViewById<TextView>(R.id.tVPlace)
        var tVDayStart: TextView = itemView.findViewById<TextView>(R.id.DayStart)
        var tVDayEnd: TextView = itemView.findViewById<TextView>(R.id.DayEnd)
        var image: ImageView= itemView.findViewById<ImageView>(R.id.logo_car)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarServiceBillAdapterViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.car_service_pay_activity,parent,false)
        return CarServiceBillAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: CarServiceBillAdapterViewHolder, position: Int) {
        val currItem: BillFragment.CarSerVice_Bill = dataList[position]
        val databaseReference = Firebase.firestore
        var car_ = currItem
        holder.tVName.text = car_.Name
        holder.tVNumSeat.text = car_.NumSeat.toString()
        holder.tVNumLuggage.text = car_.Luggage.toString()
        holder.tVCompany.text = car_.Company
        holder.tVPrice.text = formatter(car_.Price).toString() + " VND"
        holder.tVPlace.text = "Địa điểm: "+currItem.Place
        holder.tVDayStart.text = "Ngày bắt đầu: "+currItem.DayStart
        holder.tVDayEnd.text = "Ngày kết thúc: "+currItem.DayEnd
        Glide.with(mcontext).load(car_.Img)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image);
        holder.itemView.setOnClickListener {
            val builder = Dialog(mcontext)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.setTitle("Chi tiết hóa đơn")
            builder.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
            val view: View = LayoutInflater.from(mcontext).inflate(R.layout.car_bill_dialog, null)
            var Name: TextView = view.findViewById(R.id.service_car)
            var tVisDriver: TextView = view.findViewById(R.id.driver)
            var NumSeat: TextView = view.findViewById(R.id.seat_amount)
            var NumLuggage: TextView = view.findViewById(R.id.luggage_amount)
            var Company: TextView = view.findViewById(R.id.supplier)
            var Place: TextView = view.findViewById(R.id.tVPlace)
            var DayStart: TextView = view.findViewById(R.id.DayStart)
            var DayEnd: TextView = view.findViewById(R.id.DayEnd)
            var Price: TextView = view.findViewById(R.id.price)
            var imageCar: ImageView = view.findViewById(R.id.logo_car)

            Name.setText(holder.tVName.text)
            tVisDriver.setText(currItem.Type)
            NumSeat.text = holder.tVNumSeat.text
            NumLuggage.text = holder.tVNumLuggage.text
            Company.text = holder.tVCompany.text
            Place.text = holder.tVPlace.text
            DayStart.text = holder.tVDayStart.text
            DayEnd.text = holder.tVDayEnd.text
            Price.text = holder.tVPrice.text
            Glide.with(mcontext).load(car_.Img)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageCar);
            builder.setContentView(view)
            builder.show()
        }
    }
}