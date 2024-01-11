package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.FlightSearchResultAdapter
import com.example.nt118_project.Adapter.FlightTicketAdapter
import com.example.nt118_project.Adapter.HotelAdapter
import com.example.nt118_project.Adapter.HotelSearchResultAdapter
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.R
import com.example.nt118_project.flight.ListofFlightsActivity
import com.example.nt118_project.hotel.ListofHotelsActivity
import com.example.nt118_project.hotel.ListofRoomsActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import org.checkerframework.common.value.qual.StringVal
import java.time.LocalDate
import java.time.OffsetTime
import java.time.format.DateTimeFormatter


class SearchFragment : Fragment() {

    private lateinit var ed_search:EditText
    private lateinit var btn_search: AppCompatButton
    private lateinit var tv_null_info: TextView
    private lateinit var tv_null_flight: TextView
    private lateinit var tv_null_hotel: TextView
    private lateinit var tv_null_result: TextView
    private lateinit var tv_flight_result: TextView
    private lateinit var tv_hotel_result: TextView
    private lateinit var RecyclerviewFlight_result: RecyclerView
    private lateinit var RecyclerviewHotel_result:RecyclerView
    private lateinit var btn_more_flight: AppCompatButton
    private lateinit var btn_more_hotel: AppCompatButton
    private lateinit var db: FirebaseFirestore
    private lateinit var ref_flight: CollectionReference
    private lateinit var ref_hotel: CollectionReference
    private lateinit var dataList_flight_result: ArrayList<FlightTicket>
    private lateinit var dataList_hotel_result: ArrayList<Hotel>
    private lateinit var listOff_availableDate: ArrayList<String>
    private lateinit var listOff_availableTo: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
    class check_locate{
        var Locate: String = ""
        var Index: Int = -1
        constructor()
        constructor(locate:String,index:Int){
            this.Locate = locate
            this.Index = index
        }
    }
    fun Compare(checkinDay:String,currentDay:String):Boolean{
        try{
      if (checkinDay.substring(6).toInt()>currentDay.substring(6).toInt())
      {
          return true
      }
        else if (checkinDay.substring(6).toInt()==currentDay.substring(6).toInt()
          &&checkinDay.substring(3,5).toInt()>currentDay.substring(3,5).toInt()) {
            return true
        }
        else if (checkinDay.substring(6).toInt()==currentDay.substring(6).toInt()
          &&checkinDay.substring(3,5).toInt()==currentDay.substring(3,5).toInt()
          &&checkinDay.substring(0,2).toInt()>=currentDay.substring(0,2).toInt()){
            return true
        }
        return false}
        catch (e:Exception){return false}
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setcheck_locate = setOf<String>("da nang", "da lat", "ha noi","phu quoc","tp hcm","tp.hcm","sai gon","đà nẵng","đà lạt","hà nội","phú quốc","sài gòn")
        val setcheck_locate_hotel = setOf<String>("da nang", "ha noi","tp hcm","tp.hcm","sai gon","đà nẵng","hà nội","sài gòn","vung tau","vũng tàu")
        val mapcheck_locate = hashMapOf("da nang" to "Đà Nẵng (DAD)",
            "da lat" to "Đà Lạt (DLI)",
            "ha noi" to "Hà Nội (HAN)",
            "phu quoc" to "P. Quốc (PQC)",
            "tp hcm" to "TP HCM (SGN)",
            "tp.hcm" to "TP HCM (SGN)",
            "sai gon" to "TP HCM (SGN)",
            "đà nẵng" to "Đà Nẵng (DAD)",
            "đà lạt" to "Đà Lạt (DLI)",
            "hà nội" to "Hà Nội (HAN)",
            "phú quốc" to "P. Quốc (PQC)",
            "sài gòn" to "TP HCM (SGN)")
        val mapcheck_locate_hotel = hashMapOf(
            "da nang" to "Đà Nẵng",
            "ha noi" to "Hà Nội",
            "vung tau" to "Bà Rịa - Vũng Tàu",
            "tp hcm" to "Thành phố Hồ Chí Minh",
            "tp.hcm" to "Thành phố Hồ Chí Minh",
            "sai gon" to "Thành phố Hồ Chí Minh",
            "đà nẵng" to "Đà Nẵng",
            "vũng tàu" to "Bà Rịa - Vũng Tàu",
            "hà nội" to "Hà Nội",
            "sài gòn" to "Thành phố Hồ Chí Minh")
        ed_search = view.findViewById(R.id.ed_search)
        btn_search = view.findViewById(R.id.btn_search)
        tv_null_info = view.findViewById(R.id.tv_null_info)
        tv_null_flight = view.findViewById(R.id.tv_null_flight)
        tv_null_hotel = view.findViewById(R.id.tv_null_hotel)
        tv_null_result = view.findViewById(R.id.tv_null_result)
        tv_flight_result = view.findViewById(R.id.tv_flight_result)
        tv_hotel_result = view.findViewById(R.id.tv_hotel_result)
        RecyclerviewFlight_result = view.findViewById(R.id.RecyclerviewFlight_result)
        RecyclerviewHotel_result = view.findViewById(R.id.RecyclerviewHotel_result)
        btn_more_flight = view.findViewById(R.id.btn_more_flight)
        btn_more_hotel = view.findViewById(R.id.btn_more_hotel)
        db = Firebase.firestore
        ref_flight = db.collection("Flight")
        ref_hotel = db.collection("Hotel")

        val  formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val Date = currentDate
        val currentTime = OffsetTime.now()
        val nextDate =  ArrayList<String>()
        nextDate.add(Date.toString())
        for (i in 1..5){
            val tomorrow = LocalDate.now().plusDays(i.toLong()).format(formatter)
            nextDate.add(tomorrow.toString())
        }

        btn_search.setOnClickListener {
            var null_result = 0
            tv_null_flight.visibility = View.GONE
            tv_null_result.visibility = View.GONE
            tv_null_hotel.visibility = View.GONE
            tv_hotel_result.visibility =View.GONE
            tv_flight_result.visibility = View.GONE
            tv_null_info.visibility = View.GONE
            RecyclerviewFlight_result.visibility = View.GONE
            RecyclerviewHotel_result.visibility = View.GONE
            btn_more_flight.visibility = View.GONE
            btn_more_hotel.visibility = View.GONE
            var search_info = ed_search.text.toString()
            if (search_info.length>0) {
                search_info = search_info.toLowerCase()
                val indexOfFlight = search_info.indexOf("bay")
                val indexOfHotel = listOf<Int>(
                    search_info.indexOf("khach san"),
                    search_info.indexOf("khách sạn"),
                    search_info.indexOf("phòng"),
                    search_info.indexOf("phong")
                ).max()

                if (indexOfFlight != -1 || indexOfHotel == -1) {
                    val check_locate = ArrayList<check_locate>()
                    var from: String = ""
                    var to: String = ""

                    for (ele in setcheck_locate) {
                        val index = search_info.indexOf(ele)
                        if (index != -1) {
                            check_locate.add(check_locate(ele, index))
                        }
                    }
                    if (check_locate.size >= 2) {
                        if (check_locate[0].Index < check_locate[1].Index) {
                            from = mapcheck_locate[check_locate[0].Locate]!!
                            to = mapcheck_locate[check_locate[1].Locate]!!
                        } else {
                            from = mapcheck_locate[check_locate[1].Locate]!!
                            to = mapcheck_locate[check_locate[0].Locate]!!
                        }
                        dataList_flight_result = ArrayList<FlightTicket>()
                        listOff_availableDate = ArrayList<String>()
                        ref_flight.whereEqualTo("From", from).whereEqualTo("To", to)
                            .whereEqualTo("SeatClass", "Phổ thông").whereIn("Date",nextDate)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data = document.toObject<FlightTicket>()
                                    if (data.NumSeat >= 1 && Compare(data.Date, Date)) {
                                        if (data.Date!=Date||(data.Date==Date&&((data.DepartureTime.substring(0,2).toInt()-currentTime.hour.toInt()).toInt()>=1))){
                                        dataList_flight_result.add(data)
                                        listOff_availableDate.add(data.Date)}
                                    }
                                    if (dataList_flight_result.size>=3)
                                    {
                                        break
                                    }
                                }
                                if (dataList_flight_result.size == 0) {
                                    if (indexOfFlight!=-1){
                                    tv_null_flight.visibility = View.VISIBLE}
                                    else {
                                        null_result+=1
                                    }

                                } else {
                                    tv_flight_result.visibility = View.VISIBLE
                                    RecyclerviewFlight_result.visibility = View.VISIBLE
                                    btn_more_flight.visibility = View.VISIBLE
                                    var flightSearchResultAdapter = FlightSearchResultAdapter(
                                        dataList_flight_result,
                                        view.context
                                    )
                                    RecyclerviewFlight_result.adapter = flightSearchResultAdapter
                                    RecyclerviewFlight_result.layoutManager = LinearLayoutManager(
                                        view.context,
                                        LinearLayoutManager.VERTICAL, false
                                    )
                                    flightSearchResultAdapter.onItemClick =
                                        { selectedFlightTicket ->
                                            val selectedID: String = selectedFlightTicket.Id
                                            val intent =
                                                Intent(view.context, PayActivity::class.java)
                                            intent.putExtra("FirstSelectedID", selectedID)
                                            intent.putExtra("SecondSelectedID", "")
                                            intent.putExtra("Seat", "2")
                                            intent.putExtra("Tag", "Flight");
                                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                        }
                                    btn_more_flight.setOnClickListener {
                                        val intent = Intent(
                                            view.context,
                                            ListofFlightsActivity::class.java
                                        )
                                        var value = Bundle()
                                        value.putBoolean("return_check", false)
                                        value.putBoolean("is_return", false)
                                        value.putString("Date",listOff_availableDate[0])
                                        value.putString("From", from)
                                        value.putString("To", to)
                                        value.putString("NumSeat", "1")
                                        value.putString("SeatClass", "Phổ thông")
                                        intent.putExtras(value)
                                        val LAUNCH_SECOND_ACTIVITY: Int = 1
                                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Error", "Error getting documents: ", exception)
                            }
                    } else if (check_locate.size == 1) {
                            from = mapcheck_locate[check_locate[0].Locate]!!
                            dataList_flight_result = ArrayList<FlightTicket>()
                            listOff_availableTo = ArrayList<String>()
                            listOff_availableDate = ArrayList<String>()
                            ref_flight.whereEqualTo("From", from)
                            .whereEqualTo("SeatClass", "Phổ thông")
                            .whereIn("Date",nextDate)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data = document.toObject<FlightTicket>()
                                    if (data.NumSeat >= 1 && Compare(data.Date, Date)) {
                                        if (data.Date!=Date||(data.Date==Date&&((data.DepartureTime.substring(0,2).toInt()-currentTime.hour.toInt()).toInt()>=1))){
                                        dataList_flight_result.add(data)
                                        listOff_availableTo.add(data.To)
                                        listOff_availableDate.add(data.Date)}
                                    }
                                    if (dataList_flight_result.size>=3)
                                    {
                                        break
                                    }

                                }
                                if (dataList_flight_result.size == 0) {
                                    if (indexOfFlight!=-1){
                                    tv_null_flight.visibility = View.VISIBLE}
                                    else{
                                        null_result+=1
                                    }

                                } else {
                                    tv_flight_result.visibility = View.VISIBLE
                                    RecyclerviewFlight_result.visibility = View.VISIBLE
                                    btn_more_flight.visibility = View.VISIBLE
                                    var flightSearchResultAdapter = FlightSearchResultAdapter(
                                        dataList_flight_result,
                                        view.context
                                    )
                                    RecyclerviewFlight_result.adapter = flightSearchResultAdapter
                                    RecyclerviewFlight_result.layoutManager = LinearLayoutManager(
                                        view.context,
                                        LinearLayoutManager.VERTICAL, false
                                    )
                                    flightSearchResultAdapter.onItemClick =
                                        { selectedFlightTicket ->
                                            val selectedID: String = selectedFlightTicket.Id
                                            val intent =
                                                Intent(view.context, PayActivity::class.java)
                                            intent.putExtra("FirstSelectedID", selectedID)
                                            intent.putExtra("SecondSelectedID", "")
                                            intent.putExtra("Seat", "2")
                                            intent.putExtra("Tag", "Flight");
                                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                        }
                                    btn_more_flight.setOnClickListener {
                                        val intent = Intent(
                                            view.context,
                                            ListofFlightsActivity::class.java
                                        )
                                        var value = Bundle()
                                        value.putBoolean("return_check", false)
                                        value.putBoolean("is_return", false)
                                        value.putString("Date",listOff_availableDate[0])
                                        value.putString("From", from)
                                        value.putString("To", listOff_availableTo[0])
                                        value.putString("NumSeat", "1")
                                        value.putString("SeatClass", "Phổ thông")
                                        intent.putExtras(value)
                                        val LAUNCH_SECOND_ACTIVITY: Int = 1
                                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Error", "Error getting documents: ", exception)
                            }

                    } else {
                        if (indexOfFlight!=-1){
                            tv_null_flight.visibility = View.VISIBLE}
                        else{
                            Log.d("null_result","flight = 0 ${null_result}")
                            null_result+=1
                        }
                    }
                }
                if (indexOfHotel!=-1 || indexOfFlight == -1) {
                    val check_locate = ArrayList<check_locate>()
                    var locate = ""
                    for (ele in setcheck_locate_hotel) {
                        val index = search_info.indexOf(ele)
                        if (index != -1) {
                            check_locate.add(check_locate(ele, index))
                        }
                    }

                    if (check_locate.size >0) {
                        locate = mapcheck_locate_hotel[check_locate[0].Locate]!!
                        dataList_hotel_result = ArrayList<Hotel>()
                        ref_hotel.whereEqualTo("Area",locate).get().addOnSuccessListener { documents ->
                            for (document in documents) {
                                try{
                                    val data = document.toObject<Hotel>()
                                    dataList_hotel_result.add(data)
                                    if (dataList_hotel_result.size>=3) {
                                        break
                                    }
                                }
                                catch (e:Exception){
                                }
                            }
                            if (dataList_hotel_result.size == 0) {
                                if (indexOfHotel!=-1){
                                    tv_null_hotel.visibility = View.VISIBLE}
                                else{
                                    null_result+=1
                                }
                            }
                            else{
                                tv_hotel_result.visibility = View.VISIBLE
                                RecyclerviewHotel_result.visibility = View.VISIBLE
                                btn_more_hotel.visibility = View.VISIBLE
                                var hotelAdapter = HotelSearchResultAdapter(dataList_hotel_result,view.context)
                                RecyclerviewHotel_result.adapter = hotelAdapter
                                RecyclerviewHotel_result.layoutManager = LinearLayoutManager(
                                    view.context,
                                    LinearLayoutManager.VERTICAL, false
                                )
                                hotelAdapter.onItemClick = {selectedFlightTicket ->
                                    val selectedID: String = selectedFlightTicket.Id
                                    val intent = Intent(view.context, ListofRoomsActivity::class.java)
                                    intent.putExtra("SelectedID", selectedID)
                                    intent.putExtra("DayStart",Date);
                                    intent.putExtra("DayEnd",nextDate[1]);
                                    intent.putExtra("NumRoom", "1 người");
                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                btn_more_hotel.setOnClickListener {
                                    val intent = Intent(view.context, ListofHotelsActivity::class.java)
                                    intent.putExtra("Area",locate);
                                    intent.putExtra("DayStart", Date);
                                    intent.putExtra("DayEnd", nextDate[1]);
                                    intent.putExtra("NumRoom", "1 người");
                                    val LAUNCH_SECOND_ACTIVITY:Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("Firebase", "Error getting data: $exception")
                        }
                    }
                    else{
                        if (indexOfHotel!=-1){
                            tv_null_hotel.visibility = View.VISIBLE}
                        else{
                            null_result+=1
                        }
                    }
                }
                if (null_result>=2){
                    Log.d("null_result","${null_result}")
                    tv_null_result.visibility = View.VISIBLE
                }
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        if (null_result>=2){
                            Log.d("null_result","${null_result}")
                            tv_null_result.visibility = View.VISIBLE
                        }
                    },
                    2000
                )
            }
            else{
                tv_null_info.visibility = View.VISIBLE
            }
        }
    }

}