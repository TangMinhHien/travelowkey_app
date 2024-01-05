package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.RecentFlightTicketAdapter
import com.example.nt118_project.Adapter.RecentHotelAdapter
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.R
import com.example.nt118_project.flight.ListofFlightsActivity
import com.example.nt118_project.hotel.ListofHotelsActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SuggestFragment : Fragment() {

    private lateinit var RecyclerviewRecentHotel: RecyclerView
    private lateinit var RecyclerviewRecentFlightTicket:RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var ref1: CollectionReference
    private lateinit var ref2: CollectionReference
    private lateinit var data1: ArrayList<FlightTicket>
    private lateinit var data2: ArrayList<Hotel>
    private lateinit var progresssDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggest, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RecyclerviewRecentFlightTicket = view.findViewById(R.id.RecyclerviewRecentFlightTicket)
        RecyclerviewRecentHotel = view.findViewById(R.id.RecyclerviewRecentHotel)
        data1 = ArrayList<FlightTicket>()
        data2 = ArrayList<Hotel>()
        progresssDialog = ProgressDialog(view.context);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        db = Firebase.firestore
        ref1 = db.collection("Flight")
        ref1.orderBy("Price", Query.Direction.ASCENDING).limit(15)
            .get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    val data = document.toObject<FlightTicket>()
                    data1.add(data)
                }
                var recentFlightAdapter = RecentFlightTicketAdapter(
                    data1,
                    view.context
                )
                RecyclerviewRecentFlightTicket.adapter =
                    recentFlightAdapter
                RecyclerviewRecentFlightTicket.layoutManager =
                    LinearLayoutManager(
                        view.context,
                        LinearLayoutManager.VERTICAL, false
                    )
                recentFlightAdapter.onItemClick = { selectedFlightTicket ->
                    val intent = Intent(view.context,
                        ListofFlightsActivity::class.java)
                    val  formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val currentDate = LocalDate.now().format(formatter)
                    val Date = currentDate
                    var value = Bundle()
                    value.putBoolean("return_check",false)
                    value.putBoolean("is_return",false)
                    value.putString("Date",selectedFlightTicket.Date)
                    value.putString("From",selectedFlightTicket.From)
                    value.putString("To",selectedFlightTicket.To)
                    value.putString("NumSeat","1")
                    value.putString("SeatClass",selectedFlightTicket.SeatClass)
                    intent.putExtras(value)
                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
            }
        ref2 = db.collection("Hotel")
        ref2.orderBy("Price",Query.Direction.ASCENDING).limit(12)
            .get()
            .addOnSuccessListener { documents->
                for(document in documents){
                    val data = document.toObject<Hotel>()
                    data2.add(data)
                }
                progresssDialog.dismiss()
                var recentHotelAdapter = RecentHotelAdapter(
                    data2,
                    view.context
                )
                RecyclerviewRecentHotel.adapter =
                    recentHotelAdapter
                RecyclerviewRecentHotel.layoutManager =
                    LinearLayoutManager(
                        view.context,
                        LinearLayoutManager.VERTICAL, false
                    )
                recentHotelAdapter.onItemClick = { selectedHotel ->
                    val intent = Intent(view.context, ListofHotelsActivity::class.java)
                    val  formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val currentDate = LocalDate.now().format(formatter)
                    val tomorrow =  LocalDate.now().plusDays(1).format(formatter)
                    val Date = currentDate
                    val nextDate =  tomorrow
                    intent.putExtra("Area", selectedHotel.Area);
                    intent.putExtra("DayStart", Date);
                    intent.putExtra("DayEnd", nextDate);
                    intent.putExtra("NumRoom", "1 người");
                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                    startActivityForResult(
                        intent,
                        LAUNCH_SECOND_ACTIVITY
                    )
                }
            }
    }
}