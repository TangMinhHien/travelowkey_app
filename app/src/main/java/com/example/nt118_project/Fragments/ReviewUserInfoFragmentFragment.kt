package com.example.nt118_project.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Adapter.FlightTicketAdapter
import com.example.nt118_project.Adapter.FlightTicketInvoiceAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.bumptech.glide.Glide
import com.example.nt118_project.Adapter.BusTicketPayAdapter
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.Model.ServiceCar_Ticket_NoDriver
import com.example.nt118_project.Model.User
import com.google.firebase.auth.FirebaseAuth
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewUserInfoFragmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviewUserInfoFragmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_review_user_info_fragment, container, false)
        var tVNameUser:TextView = rootView.findViewById(R.id.eTFullName)
        var tVPhoneNumberUser:TextView = rootView.findViewById(R.id.eTPhoneNumber)
        var tVEmailUser:TextView = rootView.findViewById(R.id.eTEmail)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid

        val databaseReference = Firebase.firestore
        var currUser: User = User()
        databaseReference.collection("User").document(user_id).get()
            .addOnSuccessListener {document ->
                currUser = document.toObject(User::class.java)!!
                tVEmailUser.setText(currUser.Email)
                tVNameUser.setText(currUser.Name)
                tVPhoneNumberUser.setText(currUser.PhoneNumber)
                Log.d("User", tVNameUser.text.toString())
            }

        val tag_ = this.arguments?.getString("Tag")
        if(tag_ == "Bus")
        {
            val FirstID = this.arguments?.getString("FirstID")
            val SecondID = this.arguments?.getString("SecondID")
            databaseReference.collection("Bus").whereIn("Id", listOf(FirstID,SecondID))
                .get()
                .addOnSuccessListener { documents ->
                    var dataList:ArrayList<BusTicket> = ArrayList<BusTicket>().apply {
                        add(BusTicket())
                    }
                    var recyclerViewTicket: RecyclerView = rootView.findViewById<RecyclerView>(R.id.RecyclerViewTicket)
                    for (document in documents)
                    {
                        val dataModel= document.toObject(BusTicket::class.java)
                        if(dataModel.Id == FirstID)
                            dataList[0] = dataModel
                        else if(dataModel.Id == SecondID)
                            dataList.add(dataModel)
                    }
                    var busTicketAdapter = BusTicketPayAdapter(dataList)
                    recyclerViewTicket.adapter = busTicketAdapter
                    val context: Context = requireActivity()
                    recyclerViewTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                }
                .addOnFailureListener{exception ->
                    Log.w("Error getting documents: ", exception)
                }
        }
        else if(tag_ == "Flight")
        {
            val FirstID = this.arguments?.getString("FirstID")
            val SecondID = this.arguments?.getString("SecondID")
            Log.d("Flight", FirstID + " - " + SecondID)
            databaseReference.collection("Flight").whereIn("Id", listOf(FirstID,SecondID))
                .get()
                .addOnSuccessListener { documents ->
                    var dataList:ArrayList<FlightTicket> = ArrayList<FlightTicket>().apply {
                        add(FlightTicket())
                    }
                    var recyclerViewTicket: RecyclerView = rootView.findViewById<RecyclerView>(R.id.RecyclerViewTicket)
                    for (document in documents)
                    {
                        val dataModel= document.toObject(FlightTicket::class.java)
                        if(dataModel.Id == FirstID)
                            dataList[0] = dataModel
                        else if(dataModel.Id == SecondID)
                            dataList.add(dataModel)
                    }
                    var flightTicketAdapter = FlightTicketInvoiceAdapter(dataList,requireActivity())
                    recyclerViewTicket.adapter = flightTicketAdapter
                    val context: Context = requireActivity()
                    recyclerViewTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                }
                .addOnFailureListener{exception ->
                    Log.w("Error getting documents: ", exception)
                }
        }
        else if(tag_ == "Hotel")
        {
            val RelativeFrame: RelativeLayout = rootView.findViewById<RelativeLayout>(R.id.FrameViewTicket)
            var recyclerViewTicket: RecyclerView = rootView.findViewById<RecyclerView>(R.id.RecyclerViewTicket)

            val NameHotel = rootView.findViewById<TextView>(R.id.tv_name_hotel)
            val AddressHotel = rootView.findViewById<TextView>(R.id.tv_address)
            val RatingHotel = rootView.findViewById<RatingBar>(R.id.rating_bar)
            val CheckInDate = rootView.findViewById<TextView>(R.id.CheckinDate)
            val CheckOutDate = rootView.findViewById<TextView>(R.id.CheckoutDate)
            val NameRoom = rootView.findViewById<TextView>(R.id.tv_name_room)
            val Num = rootView.findViewById<TextView>(R.id.tv_num)
            val ServiceRoom = rootView.findViewById<TextView>(R.id.list_service)
            var image: ImageView = rootView.findViewById<ImageView>(R.id.image)

            recyclerViewTicket.setVisibility(View.GONE)
            RelativeFrame.setVisibility(View.VISIBLE)

            val RoomID = this.arguments?.getString("SelectedID")
            val DayStart = this.arguments?.getString("DayStart")
            val DayEnd = this.arguments?.getString("DayEnd")
            val NumRoom = this.arguments?.getString("NumRoom")
            var room_ = Room()
            var hotel_ = Hotel()
            databaseReference.collection("Room").document(RoomID.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null)
                    {
                        room_ = document.toObject(Room::class.java)!!
                        databaseReference.collection("Hotel").document(room_.Hotel_id).get()
                            .addOnSuccessListener { document ->
                                hotel_ = document.toObject(Hotel::class.java)!!
                                NameHotel.text = hotel_.Name.toString()
                                AddressHotel.text = hotel_.Address
                                RatingHotel.numStars = hotel_.Rating
                                NameRoom.text = room_.Name
                                Num.text = room_.Max.toString()+" khách/phòng"
                                ServiceRoom.text = room_.Service
                                CheckInDate.text = "Ngày nhận: " + DayStart
                                CheckOutDate.text = "Ngày trả: " + DayEnd
                                Glide.with(requireActivity()).load(room_.Img[0])
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(image);
                            }
                    }
                }
                .addOnFailureListener{exception ->
                    Log.e("TAG", "User data not found")
                }
        }
        else{
            val CarServiceFrame: ConstraintLayout = rootView.findViewById<ConstraintLayout>(R.id.FrameCarService)
            var recyclerViewTicket: RecyclerView = rootView.findViewById<RecyclerView>(R.id.RecyclerViewTicket)

            val NameCar = rootView.findViewById<TextView>(R.id.service_car)
            val SeatAmount = rootView.findViewById<TextView>(R.id.seat_amount)
            val LuggageAmount = rootView.findViewById<TextView>(R.id.luggage_amount)
            val Supplier = rootView.findViewById<TextView>(R.id.supplier)
            val tVDayStart = rootView.findViewById<TextView>(R.id.DayStart)
            val tVDayEnd = rootView.findViewById<TextView>(R.id.DayEnd)
            val tVPlace = rootView.findViewById<TextView>(R.id.tVPlace)
            val Price = rootView.findViewById<TextView>(R.id.price)
            var image: ImageView = rootView.findViewById<ImageView>(R.id.logo_car)

            recyclerViewTicket.setVisibility(View.GONE)
            CarServiceFrame.setVisibility(View.VISIBLE)

            val SelectedID = this.arguments?.getString("SelectedID")
            val DayStart = this.arguments?.getString("DayStart")
            val DayEnd = this.arguments?.getString("DayEnd")
            val TimeStart = this.arguments?.getString("TimeStart")
            val TimeEnd = this.arguments?.getString("TimeEnd")
            val PlacePick = this.arguments?.getString("PlacePick")
            val Duration = this.arguments?.getString("Duration")
            val isDriver = this.arguments?.getString("isDriver")
            var car_driver = ServiceCar_Ticket()
            var car_noDriver = ServiceCar_Ticket_NoDriver()
            val context: Context = requireActivity()
            if(isDriver == "true")
            {
                databaseReference.collection("ServiceCar_Driver").whereEqualTo("id", SelectedID).get()
                    .addOnSuccessListener { documents ->
                        for(document in documents)
                        {
                            car_driver = document.toObject(ServiceCar_Ticket::class.java)!!
                            NameCar.text = car_driver.Name.toString()
                            SeatAmount.text = car_driver.NumSeat.toString()
                            LuggageAmount.text = car_driver.NumLuggage.toString()
                            Supplier.text = car_driver.Company
                            tVDayStart.text = "Ngày bắt đầu: "+DayStart+" "+TimeStart
                            tVDayEnd.text = "Ngày kết thúc: "+DayEnd+" "+TimeEnd
                            tVPlace.text = "Địa điểm: "+PlacePick
                            Price.text = formatter(car_driver.Price).toString() + " VND/ngày"
                            Glide.with(requireActivity()).load(car_driver.image)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(image);
                        }
                    }
                    .addOnFailureListener{exception ->
                        Log.e("TAG", "User data not found")
                    }
            }
            else{
                databaseReference.collection("ServiceCar_NoDriver").whereEqualTo("Id", SelectedID).get()
                    .addOnSuccessListener { documents ->
                        for(document in documents)
                        {
                            car_noDriver = document.toObject(ServiceCar_Ticket_NoDriver::class.java)!!
                            NameCar.text = car_noDriver.Name.toString()
                            SeatAmount.text = car_noDriver.NumSeat.toString()
                            LuggageAmount.text = car_noDriver.NumLuggage.toString()
                            Supplier.text = car_noDriver.Company
                            tVDayStart.text = "Ngày bắt đầu: "+DayStart+" "+TimeStart
                            tVDayEnd.text = "Ngày kết thúc: "+DayEnd+" "+TimeEnd
                            tVPlace.text = "Địa điểm: "+PlacePick
                            Price.text = formatter(car_noDriver.Price).toString() + " VND/ngày"
                            Glide.with(requireActivity()).load(car_noDriver.image)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(image);
                        }
                    }
                    .addOnFailureListener{exception ->
                        Log.e("TAG", "User data not found")
                    }
            }
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReviewUserInfoFragmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewUserInfoFragmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}