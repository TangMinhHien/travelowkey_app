package com.example.nt118_project.flight

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.FlightTicketAdapter
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ListofFlightsActivity : AppCompatActivity() {
    private lateinit var tv_info_flight_1: TextView
    private lateinit var tv_info_flight_2: TextView
    private lateinit var RecyclerViewFlightTicket: RecyclerView
    private lateinit var tv_info_sup: TextView
    private lateinit var dataList:ArrayList<FlightTicket>
    private lateinit var DepartureDay: String
    private lateinit var ReturnDay: String
    private lateinit var db: FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var progresssDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_flights)
        val myIntent = intent
        val value: Bundle = myIntent.getExtras()!!
        val max_require = value.getString("NumSeat")!![0].digitToInt()
        Log.d("max_require","${max_require}")
        tv_info_flight_1 = findViewById(R.id.info_flight_1)
        tv_info_flight_2 = findViewById(R.id.info_flight_2)
        tv_info_sup = findViewById(R.id.info_sup_flight)
        if (value.getBoolean("is_return")) {
            tv_info_flight_1.text=value.getString("To")
            tv_info_flight_2.text = value.getString("From")
            tv_info_sup.text = value.getString("ReturnDate") + " - " + value.getString("NumSeat") + " vé - " + value.getString("SeatClass")
        }
        else {
            tv_info_flight_1.text=value.getString("From")
            tv_info_flight_2.text = value.getString("To")
            tv_info_sup.text = value.getString("Date") + " - " + value.getString("NumSeat") + " vé - " + value.getString("SeatClass")
        }

        val backBtn = findViewById<ImageButton>(R.id.list_flights_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        RecyclerViewFlightTicket = findViewById<RecyclerView>(R.id.RecyclerviewFlightTicket)
        dataList = ArrayList<FlightTicket>()
        progresssDialog = ProgressDialog(this@ListofFlightsActivity);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        db = Firebase.firestore
        ref = db.collection("Flight")
        if (!value.getBoolean("return_check")){
            ref.whereEqualTo("Date",value.getString("Date")).whereEqualTo("From",value.getString("From")).whereEqualTo("To",value.getString("To"))
                .whereEqualTo("SeatClass",value.getString("SeatClass"))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.toObject<FlightTicket>()

//                        data.setID(document.id)
                        if (data.NumSeat>=max_require){
                            dataList.add(data)
                        }
                    }
                    if (dataList.size==0){
                        progresssDialog.dismiss();
                        Toast.makeText(this, "Không tìm thấy chuyến đi phù hợp", Toast.LENGTH_LONG).show()
                    }
                    else {
                        progresssDialog.dismiss()
                        var flightTicketAdapter = FlightTicketAdapter(dataList,this@ListofFlightsActivity)
                        RecyclerViewFlightTicket.adapter = flightTicketAdapter
                        RecyclerViewFlightTicket.layoutManager = LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL, false
                        )
                        flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                            val selectedID: String = selectedFlightTicket.Id
                            val intent = Intent(this@ListofFlightsActivity,PayActivity::class.java)
                            intent.putExtra("FirstSelectedID", selectedID)
                            intent.putExtra("SecondSelectedID", "")
                            intent.putExtra("Seat",value.getString("NumSeat"))
                            intent.putExtra("Tag", "Flight");
                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Error", "Error getting documents: ", exception)
                }
        }
        else{
            if (!value.getBoolean("is_return")){
                ref.whereEqualTo("Date",value.getString("Date")).whereEqualTo("From",value.getString("From")).whereEqualTo("To",value.getString("To"))
                    .whereEqualTo("SeatClass",value.getString("SeatClass"))
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val data = document.toObject<FlightTicket>()
//                            data.setID(document.id)
                            if (data.NumSeat>=max_require){
                                dataList.add(data)
                            }
                        }
                        if (dataList.size==0){
                            progresssDialog.dismiss();
                            Toast.makeText(this, "Không tìm thấy chuyến đi phù hợp", Toast.LENGTH_LONG).show()
                        }
                        else {
                            progresssDialog.dismiss();
                            var flightTicketAdapter = FlightTicketAdapter(dataList,this@ListofFlightsActivity)
                            RecyclerViewFlightTicket.adapter = flightTicketAdapter
                            RecyclerViewFlightTicket.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL, false
                            )
                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                val selectedID: String = selectedFlightTicket.Id
                                var intent = Intent(this@ListofFlightsActivity,ListofFlightsActivity::class.java)
                                intent.putExtra("FirstSelectedID", selectedID)
                                intent.putExtra("return_check",true)
                                intent.putExtra("is_return",true)
                                intent.putExtra("Date",value.getString("Date"))
                                intent.putExtra("ReturnDate",value.getString("ReturnDate"))
                                intent.putExtra("From",value.getString("From"))
                                intent.putExtra("To",value.getString("To"))
                                intent.putExtra("NumSeat",value.getString("NumSeat"))
                                intent.putExtra("SeatClass",value.getString("SeatClass"))
                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Error", "Error getting documents: ", exception)
                    }
            }
            else{
                ref.whereEqualTo("Date",value.getString("ReturnDate")).whereEqualTo("From",value.getString("To")).whereEqualTo("To",value.getString("From"))
                    .whereEqualTo("SeatClass",value.getString("SeatClass"))
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val data = document.toObject<FlightTicket>()
//                            data.setID(document.id)
                            if (data.NumSeat>=max_require){
                                dataList.add(data)
                            }
                        }
                        if (dataList.size==0){
                            progresssDialog.dismiss();
                            Toast.makeText(this, "Không tìm thấy chuyến đi phù hợp", Toast.LENGTH_LONG).show()
                        }
                        else {
                            progresssDialog.dismiss();
                            var flightTicketAdapter = FlightTicketAdapter(dataList,this@ListofFlightsActivity)
                            RecyclerViewFlightTicket.adapter = flightTicketAdapter
                            RecyclerViewFlightTicket.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL, false
                            )
                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                val selectedID: String = selectedFlightTicket.Id
                                var intent = Intent(this@ListofFlightsActivity,PayActivity::class.java)
                                intent.putExtra("FirstSelectedID", value.getString("FirstSelectedID"))
                                intent.putExtra("SecondSelectedID", selectedID)
                                intent.putExtra("Seat",value.getString("NumSeat"))
                                intent.putExtra("Tag", "Flight")
                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Error", "Error getting documents: ", exception)
                    }
            }
        }
    }
}