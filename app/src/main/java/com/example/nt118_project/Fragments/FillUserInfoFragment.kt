package com.example.nt118_project.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
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
import com.example.nt118_project.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FillUserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FillUserInfoFragment : Fragment() {
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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_fill_user_info, container, false)
        val databaseReference = Firebase.firestore

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
                    recyclerViewTicket.setVisibility(View.VISIBLE)
                    for (document in documents)
                    {
                        val dataModel= document.toObject(BusTicket::class.java)
                        if(dataModel.Id == FirstID)
                            dataList[0] = dataModel
                        else if(dataModel.Id == SecondID)
                            dataList.add(dataModel)
                    }
                    var busTicketAdapter = BusTicketAdapter(dataList)
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
                    recyclerViewTicket.setVisibility(View.VISIBLE)
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
                            }
                    }
                }
                .addOnFailureListener{exception ->
                    Log.e("TAG", "User data not found")
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
         * @return A new instance of fragment FillUserInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FillUserInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}