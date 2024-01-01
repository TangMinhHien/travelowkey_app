package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.time.LocalDate


class ForyouFragment : Fragment() {
    private lateinit var RecyclerviewRecentHotel: RecyclerView
    private lateinit var RecyclerviewRecentFlightTicket: RecyclerView
    private lateinit var db:FirebaseFirestore
    private lateinit var hotel_ref: CollectionReference
    private lateinit var hotel_ref2: CollectionReference
    private lateinit var hotel_ref3: CollectionReference
    private lateinit var hotel_ref4: CollectionReference
    private lateinit var hotel_ref5: CollectionReference
    private lateinit var hotel_dataList:ArrayList<String>
    private lateinit var hotel_dataList2:ArrayList<String>
    private lateinit var hotel_dataList3:ArrayList<String>
    private lateinit var hotel_dataList4:ArrayList<String>
    private lateinit var hotel_dataList5:ArrayList<Hotel>
    private lateinit var flight_ref: CollectionReference
    private lateinit var flight_ref2: CollectionReference
    private lateinit var flight_ref3: CollectionReference
    private lateinit var flight_ref4: CollectionReference
    private lateinit var flight_dataList:ArrayList<String>
    private lateinit var flight_dataList2:ArrayList<String>
    private lateinit var flight_dataList3:ArrayList<String>
    private lateinit var flight_dataList4: ArrayList<FlightTicket>
    private lateinit var progresssDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foryou, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotel_dataList = ArrayList<String>()
        hotel_dataList2 = ArrayList<String>()
        hotel_dataList3 = ArrayList<String>()
        hotel_dataList4 = ArrayList<String>()
        hotel_dataList5 = ArrayList<Hotel>()
        flight_dataList = ArrayList<String>()
        flight_dataList2 = ArrayList<String>()
        flight_dataList3 = ArrayList<String>()
        flight_dataList4 = ArrayList<FlightTicket>()
        RecyclerviewRecentHotel = view.findViewById(R.id.RecyclerviewRecentHotel)
        RecyclerviewRecentFlightTicket = view.findViewById(R.id.RecyclerviewRecentFlightTicket)
        progresssDialog = ProgressDialog(view.context);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid as String
            db = Firebase.firestore
            hotel_ref = db.collection("Invoice")
            hotel_ref.whereEqualTo("tag","Hotel").whereEqualTo("user_Id",uid).limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.id
                        hotel_dataList.add(data)
                    }
                    Log.d("check_data","${hotel_dataList.size}")
                    Log.d("check_data","${hotel_dataList.toString()}")
                    if (hotel_dataList.size>0) {
                        hotel_ref2 = db.collection("Hotel_invoice")
                        hotel_ref2.whereIn("invoice_Id", hotel_dataList)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data2 = document["roomId"] as String
                                    hotel_dataList2.add(data2)
                                }
                                Log.d("check_data","${hotel_dataList2.size}")
                                Log.d("check_data","${hotel_dataList2.toString()}")
                                hotel_ref3 = db.collection("Room")
                                hotel_ref3.whereIn("Id", hotel_dataList2)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val data3 = document["Hotel_id"] as String
                                            hotel_dataList3.add(data3)
                                        }
                                        Log.d("check_data","${hotel_dataList3.size}")
                                        Log.d("check_data","${hotel_dataList3.toString()}")
                                        hotel_ref4 = db.collection("Hotel")
                                        hotel_ref4.whereIn("Id",hotel_dataList3)
                                            .get()
                                            .addOnSuccessListener { documents ->
                                                for (document in documents) {
                                                    val data = document["Area"] as String
                                                    hotel_dataList4.add(data)
                                                }
                                                Log.d("check_data","${hotel_dataList4.size}")
                                                Log.d("check_data","${hotel_dataList4.toString()}")
                                                hotel_ref5 = db.collection("Hotel")
                                                hotel_ref5.whereIn("Area",hotel_dataList4).limit(12)
                                                    .get()
                                                    .addOnSuccessListener { documents->
                                                        for (document in documents){
                                                            val data = document.toObject<Hotel>()
                                                            hotel_dataList5.add(data)
                                                        }
                                                        Log.d("check_data","${hotel_dataList5.size}")
                                                        Log.d("check_data","${hotel_dataList5.toString()}")
                                                        progresssDialog.dismiss()
                                                        var recentHotelAdapter = RecentHotelAdapter(
                                                            hotel_dataList5,
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
                                                            val currentDate = LocalDate.now()
                                                            val currentDay = currentDate.dayOfMonth
                                                            val currentMonth = currentDate.monthValue
                                                            val currentYear = currentDate.year
                                                            val Day_start =
                                                                currentDay.toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()
                                                            val Day_end =
                                                                (currentDay+1).toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()
                                                            intent.putExtra("Area", selectedHotel.Area);
                                                            intent.putExtra("DayStart", Day_start);
                                                            intent.putExtra("DayEnd", Day_end);
                                                            intent.putExtra("NumRoom", "1");
                                                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                            startActivityForResult(
                                                                intent,
                                                                LAUNCH_SECOND_ACTIVITY
                                                            )
                                                        }
                                                    }
                                                    .addOnFailureListener { Log.d("check_data","Failed") }
                                            }
                                    }
                            }
                    }
                    else{
                        hotel_ref2 = db.collection("Hotel")
                        hotel_ref2.orderBy("Price", Query.Direction.ASCENDING).limit(12)
                            .get()
                            .addOnSuccessListener { documents->
                                for(document in documents){
                                    val data = document.toObject<Hotel>()
                                    hotel_dataList5.add(data)
                                }
                                Log.d("check_data","${hotel_dataList5.size}")
                                Log.d("check_data","${hotel_dataList5.toString()}")
                                progresssDialog.dismiss()
                                var recentHotelAdapter = RecentHotelAdapter(
                                    hotel_dataList5,
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
                                    val currentDate = LocalDate.now()
                                    val currentDay = currentDate.dayOfMonth
                                    val currentMonth = currentDate.monthValue
                                    val currentYear = currentDate.year
                                    val Day_start =
                                        currentDay.toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()
                                    val Day_end =
                                        (currentDay+1).toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()
                                    intent.putExtra("Area", selectedHotel.Area);
                                    intent.putExtra("DayStart", Day_start);
                                    intent.putExtra("DayEnd", Day_end);
                                    intent.putExtra("NumRoom", "1");
                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                    startActivityForResult(
                                        intent,
                                        LAUNCH_SECOND_ACTIVITY
                                    )
                                }
                            }
                    }
                }
            flight_ref = db.collection("Invoice")
            flight_ref.whereEqualTo("tag","Flight").whereEqualTo("user_Id",uid).limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.id
                        flight_dataList.add(data)
                    }
                    if (flight_dataList.size>0) {
                        flight_ref2 = db.collection("Flight_invoice")
                        flight_ref2.whereIn("invoice_Id", flight_dataList)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data2 = document["id_ticket_1"] as String
                                    flight_dataList2.add(data2)
                                }
                                flight_ref3 = db.collection("Flight")
                                flight_ref3.whereIn("Id", flight_dataList2)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val data3 = document["From"] as String
                                            flight_dataList3.add(data3)
                                        }
                                        flight_ref4 = db.collection("Flight")
                                        flight_ref4.whereIn("From",flight_dataList3).limit(15)
                                            .get()
                                            .addOnSuccessListener { documents->
                                                for (document in documents){
                                                    val data = document.toObject<FlightTicket>()
                                                    flight_dataList4.add(data)
                                                }
                                                progresssDialog.dismiss()
                                                var recentFlightAdapter = RecentFlightTicketAdapter(
                                                    flight_dataList4,
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
                                                    val currentDate = LocalDate.now()
                                                    val currentDay = currentDate.dayOfMonth
                                                    val currentMonth = currentDate.monthValue
                                                    val currentYear = currentDate.year
                                                    val Date = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
                                                    var value = Bundle()
                                                    value.putBoolean("return_check",false)
                                                    value.putBoolean("is_return",false)
                                                    value.putString("Date",Date)
                                                    value.putString("From",selectedFlightTicket.From)
                                                    value.putString("To",selectedFlightTicket.To)
                                                    value.putString("NumSeat","1")
                                                    value.putString("SeatClass",selectedFlightTicket.SeatClass)
                                                    intent.putExtras(value)
                                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                            }
                                    }
                            }
                    }
                    else {
                        flight_ref2 = db.collection("Flight")
                        flight_ref2.orderBy("Price", Query.Direction.ASCENDING).limit(15)
                            .get()
                            .addOnSuccessListener { documents->
                                for (document in documents){
                                    val data = document.toObject<FlightTicket>()
                                    flight_dataList4.add(data)
                                }
                                progresssDialog.dismiss()
                                var recentFlightAdapter = RecentFlightTicketAdapter(
                                    flight_dataList4,
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
                                    val currentDate = LocalDate.now()
                                    val currentDay = currentDate.dayOfMonth
                                    val currentMonth = currentDate.monthValue
                                    val currentYear = currentDate.year
                                    val Date = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
                                    var value = Bundle()
                                    value.putBoolean("return_check",false)
                                    value.putBoolean("is_return",false)
                                    value.putString("Date",Date)
                                    value.putString("From",selectedFlightTicket.From)
                                    value.putString("To",selectedFlightTicket.To)
                                    value.putString("NumSeat","1")
                                    value.putString("SeatClass",selectedFlightTicket.SeatClass)
                                    intent.putExtras(value)
                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                            }
                    }
                }
        } else {
        }
    }
}