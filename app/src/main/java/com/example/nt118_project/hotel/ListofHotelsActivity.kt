package com.example.nt118_project.hotel

import android.app.ProgressDialog
import android.content.Intent
import android.location.Address
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.FlightTicketAdapter
import com.example.nt118_project.Adapter.HotelAdapter
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.R
import com.example.nt118_project.flight.ListofFlightsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class ListofHotelsActivity : AppCompatActivity() {
    private lateinit var tv_info_Hotel: TextView
    private lateinit var RecyclerViewHotels: RecyclerView
    private lateinit var tv_info_sup: TextView
    private lateinit var dataList:ArrayList<Hotel>
    private lateinit var f_dataList:ArrayList<Hotel>
    private lateinit var db:FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var hotelAdapter:HotelAdapter
    private lateinit var progresssDialog: ProgressDialog
    private lateinit var sorting_btn: FloatingActionButton
    private lateinit var tv_notfound: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_hotels)

        val myIntent = intent
        val value: Bundle = myIntent.getExtras()!!

        tv_info_Hotel = findViewById(R.id.info_hotel)
        tv_info_Hotel.text = value.getString("Area")
        tv_info_sup = findViewById(R.id.info_sup_hotel)
        tv_info_sup.text = value.getString("DayStart") + " - " + value.getString("DayEnd") + " - " + value.getString("NumRoom")+"/phòng"
        tv_notfound = findViewById(R.id.result_not_found)
        sorting_btn = findViewById(R.id.sorting_button)
        RecyclerViewHotels = findViewById(R.id.RecyclerviewHotel)
        dataList = ArrayList<Hotel>()
        progresssDialog = ProgressDialog(this@ListofHotelsActivity);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();

        db = Firebase.firestore
        ref = db.collection("Hotel")
        ref.whereEqualTo("Area",value.getString("Area")).get().addOnSuccessListener { documents ->
            for (document in documents) {
                try{
                    Log.d("Hotel","${document.data}")
                val Data = document.toObject<Hotel>()
//                Data.setID(document.id)
                dataList.add(Data)}
                catch (e:Exception){
                    Log.d("Fail","Can't get data")
                }
            }
            if (dataList.size == 0) {
                progresssDialog.dismiss()
                tv_notfound.visibility = View.VISIBLE
            }
            else{
                progresssDialog.dismiss()
                hotelAdapter = HotelAdapter(dataList,this@ListofHotelsActivity)
                RecyclerViewHotels.adapter = hotelAdapter
                RecyclerViewHotels.layoutManager = LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL, false
                )
                hotelAdapter.onItemClick = {selectedFlightTicket ->
                    val selectedID: String = selectedFlightTicket.Id
                    val intent = Intent(this@ListofHotelsActivity, ListofRoomsActivity::class.java)
                    intent.putExtra("SelectedID", selectedID)
                    intent.putExtra("DayStart", value.getString("DayStart"));
                    intent.putExtra("DayEnd",value.getString("DayEnd"));
                    intent.putExtra("NumRoom", value.getString("NumRoom"));
                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                sorting_btn.setOnClickListener {
                    val popupMenu = PopupMenu(this, it)
                    popupMenu.inflate(R.menu.menu_sorting)
                    var newDataOfRecyclerView:List<Hotel>
                    newDataOfRecyclerView = dataList
                    popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                        when (item.itemId) {
                            R.id.descending_sort -> {
                                var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                val hotelAdapter = HotelAdapter(newDataOfRecyclerView_,this@ListofHotelsActivity)
                                RecyclerViewHotels.adapter = hotelAdapter
                                RecyclerViewHotels.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                hotelAdapter.onItemClick = {selectedFlightTicket ->
                                    val selectedID: String = selectedFlightTicket.Id
                                    val intent = Intent(this@ListofHotelsActivity, ListofRoomsActivity::class.java)
                                    intent.putExtra("SelectedID", selectedID)
                                    intent.putExtra("DayStart", value.getString("DayStart"));
                                    intent.putExtra("DayEnd",value.getString("DayEnd"));
                                    intent.putExtra("NumRoom", value.getString("NumRoom"));
                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                true
                            }
                            R.id.ascending_sort -> {
                                var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                val hotelAdapter = HotelAdapter(newDataOfRecyclerView_,this@ListofHotelsActivity)
                                RecyclerViewHotels.adapter = hotelAdapter
                                RecyclerViewHotels.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                hotelAdapter.onItemClick = {selectedFlightTicket ->
                                    val selectedID: String = selectedFlightTicket.Id
                                    val intent = Intent(this@ListofHotelsActivity, ListofRoomsActivity::class.java)
                                    intent.putExtra("SelectedID", selectedID)
                                    intent.putExtra("DayStart", value.getString("DayStart"));
                                    intent.putExtra("DayEnd",value.getString("DayEnd"));
                                    intent.putExtra("NumRoom", value.getString("NumRoom"));
                                    val LAUNCH_SECOND_ACTIVITY: Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error getting data: $exception")
        }

        val backBtn = findViewById<ImageButton>(R.id.list_hotels_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}