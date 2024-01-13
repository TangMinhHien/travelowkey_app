package com.example.nt118_project.service_car

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.Model.ServiceCar_Ticket_NoDriver
import com.example.nt118_project.R
import java.text.DecimalFormat

class ServiceCar_Ticket_NoDriver_Adapter (private var dataList: ArrayList<ServiceCar_Ticket_NoDriver>, private var context: Context): RecyclerView.Adapter<ServiceCar_Ticket_NoDriver_Adapter.ServiceCar_ViewHolder>() {
    //    val Id: String
    public var onItemClick: ((ServiceCar_Ticket_NoDriver) -> Unit)? = null
    inner class ServiceCar_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVName: TextView = itemView.findViewById<TextView>(R.id.service_car)
        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.price)
        var tVNumSupplier: TextView = itemView.findViewById<TextView>(R.id.supplier)
        var tVNumLuggage: TextView = itemView.findViewById<TextView>(R.id.luggage_amount)
        var tVNumSeat: TextView = itemView.findViewById<TextView>(R.id.seat_amount)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.logo_car)
//        val dictionary = hashMapOf("Hyundai Grand i10" to "https://ik.imagekit.io/tvlk/image/imageResource/2021/11/18/1637208296447-606cf4459b8f5abcde719a333e019725.jpeg?tr=q-75,w-140"
//            , "VinFast Fadil" to "https://ik.imagekit.io/tvlk/image/imageResource/2021/11/18/1637208308735-14c75db4b125d8cc4a19d7b6f6906e96.jpeg?tr=q-75,w-140"
//            ,"Toyota Vios" to "https://ik.imagekit.io/tvlk/image/imageResource/2021/11/18/1637208345239-adfca7e6d8f35ad5ef953e0ca754432f.jpeg?tr=q-75,w-140"
//            ,"Kia Cerato" to "https://ik.imagekit.io/tvlk/image/imageResource/2021/11/18/1637209686570-fc1c1975f622f1d8c26df24372fd239a.jpeg?tr=q-75,w-140")
//        val set = setOf("Hyundai Grand i10","VinFast Fadil","Toyota Vios","Kia Cerato")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceCar_ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_list_of_servicecar,parent,false)
        return ServiceCar_ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ServiceCar_ViewHolder, position: Int) {
        val currItem: ServiceCar_Ticket_NoDriver = dataList [position]
        holder.tVName.setText(currItem.Name)
        holder.tVNumLuggage.setText(currItem.NumLuggage.toString())
        holder.tVNumSeat.setText(currItem.NumSeat.toString())
        holder.tVNumSupplier.setText(currItem.Company)
        holder.tVPrice.setText("Từ "+formatter(currItem.Price).toString() + " VND/khách")

//        var value: String = holder.dictionary["VinFast Fadil"]!!
//        if (holder.set.contains(currItem.CarName)){
//            value = holder.dictionary[currItem.CarName]!!
//        }

        Glide.with(context).load(currItem.image)
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