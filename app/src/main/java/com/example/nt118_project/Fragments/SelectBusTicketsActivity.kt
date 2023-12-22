package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.MainActivity
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.locks.ReentrantLock

class SelectBusTicketsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var tv_search: TextView
    private lateinit var RecyclerViewBusTicket: RecyclerView
    private lateinit var tvSeat: TextView
    private lateinit var DepartureDaytV: TextView
    private lateinit var dataList:ArrayList<BusTicket>
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var progresssDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bus_tickets)
        val reentrantLock = ReentrantLock()
        val myIntent = intent
        val startingpoint:String = myIntent.getStringExtra("Starting Point").toString()
        val destinationpoint:String = myIntent.getStringExtra("Destination Point").toString()
        val ReturnBtn = findViewById<ImageView>(R.id.iVBack)
        progresssDialog = ProgressDialog(this);
        tv_search = findViewById<TextView>(R.id.tv_search)
        DepartureDaytV = findViewById<TextView>(R.id.DepartureDaySpinner)
        tvSeat = findViewById<TextView>(R.id.tVSeat)
        spinner1 = findViewById<Spinner>(R.id.SpinnerFrom)
        spinner2 = findViewById<Spinner>(R.id.SpinnerTo)
        tv_search.text = startingpoint + " -> " + destinationpoint
        DepartureDaytV.text = myIntent.getStringExtra("DepartTime").toString()
        tvSeat.text = myIntent.getStringExtra("Seat").toString()
        spinner1.onItemSelectedListener = this
        spinner2.onItemSelectedListener = this

        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        RecyclerViewBusTicket = findViewById<RecyclerView>(R.id.RecyclerViewBusTicket)
        reentrantLock.lock()
        dataList = ArrayList<BusTicket>()
        val databaseReference = Firebase.firestore
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val departTime = myIntent.getStringExtra("DepartTime").toString()
        val date = inputFormat.parse(departTime)
        val Date= outputFormat.format(date!!)
        Log.d("Date",Date)
        databaseReference.collection("Bus").whereEqualTo("From", startingpoint).whereEqualTo("To", destinationpoint)
            .whereEqualTo("Date", Date)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    val dataModel= document.toObject(BusTicket::class.java)
                    if (dataModel.NumSeat.toDouble() > 0)
                        dataList.add(dataModel)
                }
                if (dataList.size == 0)
                {
                    progresssDialog.dismiss();
                    Toast.makeText(this@SelectBusTicketsActivity, "Không tìm thấy chuyến đi phù hợp", Toast.LENGTH_LONG).show()
                }
                else
                {
                    progresssDialog.dismiss();
                    var busTicketAdapter = BusTicketAdapter(dataList)
                    RecyclerViewBusTicket.adapter = busTicketAdapter
                    RecyclerViewBusTicket.layoutManager = LinearLayoutManager(this@SelectBusTicketsActivity,LinearLayoutManager.VERTICAL,false)
                    val myExtraBoolean = intent.getBooleanExtra("RoundTrip", false)
                    busTicketAdapter.onItemClick = {selectedBusTicket ->
                        val selectedID:String = selectedBusTicket.Id
                        Log.d("FirstID", selectedID)
                        if(myExtraBoolean)
                        {
                            val intent = Intent(this@SelectBusTicketsActivity, SelectBusTickets_2Activity::class.java)
                            intent.putExtra("Starting Point", startingpoint);
                            intent.putExtra("Destination Point", destinationpoint);
                            intent.putExtra("DepartTime", DepartureDaytV.text.toString());
                            intent.putExtra("ReturnTime", myIntent.getStringExtra("ReturnTime").toString());
                            intent.putExtra("Seat", myIntent.getStringExtra("Seat").toString());
                            intent.putExtra("FirstSelectedID", selectedID);
                            intent.putExtra("SecondSelectedID", "");
                            intent.putExtra("RoundTrip", myExtraBoolean);
                            val LAUNCH_SECOND_ACTIVITY:Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                        }
                        else
                        {
                            val intent = Intent(this@SelectBusTicketsActivity, PayActivity::class.java)
                            intent.putExtra("FirstSelectedID", selectedID);
                            intent.putExtra("SecondSelectedID", "");
                            intent.putExtra("Seat", myIntent.getStringExtra("Seat").toString());
                            intent.putExtra("RoundTrip", myExtraBoolean);
                            val LAUNCH_SECOND_ACTIVITY:Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                        }
                    }
                }
            }
            .addOnFailureListener{exception ->
                Log.w("Error getting documents: ", exception)
            }
        reentrantLock.unlock()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Selected","Select Seat")
        (view as TextView).setTextColor(Color.WHITE)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("Selected","Not select seat")
    }
}