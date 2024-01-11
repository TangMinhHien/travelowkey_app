package com.example.nt118_project.Adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
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
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.text.DecimalFormat

class RoomTicketInvoiceBillAdapter(private var dataList: ArrayList<HotelTicketInvoice>, private var mcontext: Context): RecyclerView.Adapter<RoomTicketInvoiceBillAdapter.RoomTicketInvoiceBillAdapterViewHolder>() {
    public var onItemClick: ((BusTicket) -> Unit)? = null
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
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
                var SerVice: String = ""
                var image_hotel:String = ""
                var image_room:String = ""
                for (document in documents)
                {
                    var room_ = document.toObject(Room::class.java)
                    image_room = room_.Img[0]
                    databaseReference.collection("Hotel").document(room_.Hotel_id).get()
                        .addOnSuccessListener { document ->
                            var hotel_ = document.toObject(Hotel::class.java)
                            SerVice = room_.Service
                            holder.tVNameHotel.setText(hotel_!!.Name.toString())
                            holder.tVAddressHotel.setText(hotel_!!.Address)
                            holder.tVRatingBar.numStars = hotel_!!.Rating
                            holder.tVNameRoom.setText(room_.Name.toString())
                            holder.tVNum.setText(room_.Max.toString()+" khách/phòng")
                            holder.tVNumCus.setText("Số khách: " + currItem.NumCus)
                            holder.tVPrice.setText(this.formatter(currItem.Price.toDouble().toInt()).toString() + "VND")
                            holder.tVCheckinDate.setText("Ngày nhận phòng: "+ currItem.ChechInDate)
                            holder.tVCheckoutDate.setText("Ngày trả phòng: "+ currItem.ChechOutDate)
                            image_hotel = hotel_!!.Img
                        }
                }
                holder.itemView.setOnClickListener {
                    val builder = Dialog(mcontext)
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    builder.setTitle("Chi tiết hóa đơn")
                    builder.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
                    val view: View =
                        LayoutInflater.from(mcontext).inflate(R.layout.hotel_bill_dialog, null)
                    var NameHotel:TextView = view.findViewById(R.id.tv_name_hotel)
                    var AddressHotel:TextView = view.findViewById(R.id.tv_address)
                    var RatingStar:RatingBar = view.findViewById(R.id.rating_bar)
                    var CheckInDate:TextView = view.findViewById(R.id.CheckinDate)
                    var CheckOutDate:TextView = view.findViewById(R.id.CheckoutDate)
                    var NameRoom:TextView = view.findViewById(R.id.tv_name_room)
                    var MaxNum: TextView = view.findViewById(R.id.tv_num)
                    var Service: TextView = view.findViewById(R.id.tvService)
                    var NumCus: TextView = view.findViewById(R.id.tv_numCus)
                    var Price: TextView = view.findViewById(R.id.tVPrice)
                    var imageHotel: ImageView = view.findViewById(R.id.imageHotel)
                    var imageRoom: ImageView = view.findViewById(R.id.imageRoom)

                    NameHotel.setText(holder.tVNameHotel.text)
                    AddressHotel.setText(holder.tVAddressHotel.text)
                    RatingStar.numStars = holder.tVRatingBar.numStars
                    CheckInDate.text = holder.tVCheckinDate.text
                    CheckOutDate.text = holder.tVCheckoutDate.text
                    NameRoom.text = holder.tVNameRoom.text
                    MaxNum.text = holder.tVNum.text
                    Service.text = SerVice
                    NumCus.text = holder.tVNumCus.text
                    Price.text = holder.tVPrice.text
                    Glide.with(mcontext).load(image_hotel)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageHotel);
                    Glide.with(mcontext).load(image_room)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageRoom);
                    builder.setContentView(view)
                    builder.show()
                }
            }
    }
}