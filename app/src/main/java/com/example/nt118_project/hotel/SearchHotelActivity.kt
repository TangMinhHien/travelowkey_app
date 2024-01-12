package com.example.nt118_project.hotel

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.RecentFlightTicketAdapter
import com.example.nt118_project.Adapter.RecentHotelAdapter
import com.example.nt118_project.Fragments.SelectBusTicketsActivity
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.example.nt118_project.flight.ListofFlightsActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class SearchHotelActivity : AppCompatActivity() {
    private lateinit var DayStart: TextView
    private lateinit var DayEnd: TextView
    private lateinit var SearchButton: Button
    private lateinit var Area: Spinner
    private lateinit var SpinnerNumber: Spinner
    private lateinit var db:FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var ref2: CollectionReference
    private lateinit var ref3: CollectionReference
    private lateinit var ref4: CollectionReference
    private lateinit var dataList:ArrayList<String>
    private lateinit var dataList2:ArrayList<String>
    private lateinit var dataList3:ArrayList<String>
    private lateinit var dataList4:ArrayList<Hotel>
    private lateinit var RecyclerviewRecentHotel: RecyclerView
    private lateinit var progresssDialog: ProgressDialog
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_hotel)

        dataList = ArrayList<String>()
        dataList2 = ArrayList<String>()
        dataList3 = ArrayList<String>()
        dataList4 = ArrayList<Hotel>()
        RecyclerviewRecentHotel = findViewById(R.id.RecyclerviewRecentHotel)
        progresssDialog = ProgressDialog(this@SearchHotelActivity);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid as String
            db = Firebase.firestore
            ref = db.collection("Invoice")
            ref.whereEqualTo("tag","Hotel").whereEqualTo("user_Id",uid).limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.id
                        dataList.add(data)
                    }
                    if (dataList.size>0) {
                        ref2 = db.collection("Hotel_invoice")
                        ref2.whereIn("invoice_Id", dataList)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data2 = document["roomId"] as String
                                    dataList2.add(data2)
                                }
                                ref3 = db.collection("Room")
                                ref3.whereIn("Id", dataList2)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val data3 = document["Hotel_id"] as String
                                            dataList3.add(data3)
                                        }
                                        ref4 = db.collection("Hotel")
                                        ref4.whereIn("Id",dataList3)
                                            .get()
                                            .addOnSuccessListener { documents ->
                                                for (document in documents) {
                                                    val data = document.toObject<Hotel>()
                                                    dataList4.add(data)
                                                }
                                                progresssDialog.dismiss()
                                                var recentHotelAdapter = RecentHotelAdapter(
                                                    dataList4,
                                                    this@SearchHotelActivity
                                                )
                                                RecyclerviewRecentHotel.adapter =
                                                    recentHotelAdapter
                                                RecyclerviewRecentHotel.layoutManager =
                                                    LinearLayoutManager(
                                                        this,
                                                        LinearLayoutManager.HORIZONTAL, false
                                                    )
                                                recentHotelAdapter.onItemClick = { selectedHotel ->
                                                    val intent = Intent(this@SearchHotelActivity, ListofHotelsActivity::class.java)
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
                    }
                    progresssDialog.dismiss()
                }
        } else {
        }

        val backBtn = findViewById<ImageButton>(R.id.search_hotel_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        DayStart = findViewById<TextView>(R.id.sp_date_start)
        DayEnd = findViewById<TextView>(R.id.sp_dat_end)
        val c: Calendar = Calendar.getInstance()
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DayStart)
        }
        val datepicker_return = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DayEnd)
        }
        DayStart.setOnClickListener {
            val DatePickerDialog = DatePickerDialog(this, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }
        DayEnd.setOnClickListener {
            val DatePickerDialog= DatePickerDialog(this, datepicker_return, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }
//        val currentDate = LocalDate.now()
//        val currentDay = currentDate.dayOfMonth
//        val currentMonth = currentDate.monthValue
//        val currentYear = currentDate.year
//        DayStart.text = "0" + currentDay.toString()+"-"+"0" + currentMonth.toString()+"-"+currentYear.toString()
//        DayEnd.text = "0" + currentDay.toString()+"-"+"0" + currentMonth.toString()+"-"+currentYear.toString()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDay = LocalDateTime.now().format(formatter)
        DayStart.text = currentDay
        DayEnd.text = currentDay

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        Area = findViewById<Spinner>(R.id.sp_address)
        val AreaData: ArrayList<Any?> = ArrayList()
        db = Firebase.firestore
        ref = db.collection("Hotel_area")
        ref.get().addOnSuccessListener { documents->
            for (document in documents){
                AreaData.add(document.get("Area"))
            }
            val AreaAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this,android.R.layout.simple_spinner_item,AreaData)
            AreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            Area.onItemSelectedListener = listener
            Area.setAdapter(AreaAdapter)
        }


        SpinnerNumber = findViewById<Spinner>(R.id.sp_room)
        val NumberSpinnerData: ArrayList<Any?> = ArrayList()
        NumberSpinnerData.add("1 người")
        NumberSpinnerData.add("2 người")
        NumberSpinnerData.add("3 người")
        NumberSpinnerData.add("4 người")
        val NumberAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this,android.R.layout.simple_spinner_item,NumberSpinnerData)
        NumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerNumber.onItemSelectedListener = listener
        SpinnerNumber.setAdapter(NumberAdapter)
        SearchButton = findViewById(R.id.btn_search)
        SearchButton.setOnClickListener {
            val current = LocalDateTime.now()
            val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val dateStart = LocalDate.parse(DayStart.text, sdf)
            val dateReturn = LocalDate.parse(DayEnd.text, sdf)

            val result = dateStart.compareTo(dateReturn)
            if (result >= 0) {
                Toast.makeText(this, "Vui lòng chọn ngày thuê trước ngày trả phòng", Toast.LENGTH_LONG).show()
            }
            else
            {
                val intent = Intent(this@SearchHotelActivity, ListofHotelsActivity::class.java)
                intent.putExtra("Area", Area.getSelectedItem().toString());
                intent.putExtra("DayStart", DayStart.text.toString());
                intent.putExtra("DayEnd", DayEnd.text.toString());
                intent.putExtra("NumRoom", SpinnerNumber.getSelectedItem().toString());
                val LAUNCH_SECOND_ACTIVITY:Int = 1
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
            }
        }
    }
    private fun updateLable(c: Calendar, tV: TextView) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText(sdf.format(c.time))
    }
}