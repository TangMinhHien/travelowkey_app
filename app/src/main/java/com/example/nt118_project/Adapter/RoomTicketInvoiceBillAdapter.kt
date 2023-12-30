package com.example.nt118_project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.BusTicketInvoice
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.HotelTicketInvoice
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class RoomTicketInvoiceBillAdapter(private var dataList: ArrayList<HotelTicketInvoice>): RecyclerView.Adapter<RoomTicketInvoiceBillAdapter.RoomTicketInvoiceBillAdapterViewHolder>() {
    public var onItemClick: ((BusTicket) -> Unit)? = null
    inner class RoomTicketInvoiceBillAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tVNameHotel: TextView = itemView.findViewById<TextView>(R.id.tv_name_hotel)
        var tVAddressHotel: TextView = itemView.findViewById<TextView>(R.id.tv_address)
        var tVRatingBar: RatingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
        var tVCheckinDate: TextView = itemView.findViewById<TextView>(R.id.CheckinDate)
        var tVCheckoutDate: TextView = itemView.findViewById<TextView>(R.id.CheckoutDate)

        var tVNameRoom: TextView = itemView.findViewById<TextView>(R.id.tv_name_room)
        var tVNum: TextView = itemView.findViewById<TextView>(R.id.tv_num)
        var tVNumCus: TextView = itemView.findViewById<TextView>(R.id.tv_numCus)

        var tVPrice: TextView = itemView.findViewById<TextView>(R.id.tVPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomTicketInvoiceBillAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_hotel_bill,parent,false)
        return RoomTicketInvoiceBillAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RoomTicketInvoiceBillAdapterViewHolder, position: Int) {
        val currItem: HotelTicketInvoice = dataList[position]
        val databaseReference = Firebase.firestore
        databaseReference.collection("Room").whereEqualTo("Id", currItem.RoomID).get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    var room_ = document.toObject(Room::class.java)
                    databaseReference.collection("Hotel").document(room_.Hotel_id).get()
                        .addOnSuccessListener { document ->
                            var hotel_ = document.toObject(Hotel::class.java)
                            holder.tVNameHotel.setText(hotel_!!.Name.toString())
                            holder.tVAddressHotel.setText(hotel_!!.Address)
                            holder.tVRatingBar.numStars = hotel_!!.Rating
                            holder.tVNameRoom.setText(room_.Name.toString())
                            holder.tVNum.setText(room_.Max.toString()+" khách/phòng")
                            holder.tVNumCus.setText("Số khách: " + currItem.NumCus)
                            holder.tVPrice.setText(currItem.Price + "VND")
                            holder.tVCheckinDate.setText("Ngày nhận: "+ currItem.ChechInDate)
                            holder.tVCheckoutDate.setText("Ngày trả: "+ currItem.ChechOutDate)
                        }
                }
            }
    }
}