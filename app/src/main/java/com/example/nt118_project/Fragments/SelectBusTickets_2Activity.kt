package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.locks.ReentrantLock

class SelectBusTickets_2Activity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
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
        setContentView(R.layout.activity_select_bus_tickets2)

        val myIntent = intent // this is just for example purpose
        val startingpoint: String = myIntent.getStringExtra("Starting Point").toString()
        val destinationpoint: String = myIntent.getStringExtra("Destination Point").toString()
        val ReturnBtn = findViewById<ImageView>(R.id.iVBack)
        progresssDialog = ProgressDialog(this)
        tv_search = findViewById<TextView>(R.id.tv_search)
        DepartureDaytV = findViewById<TextView>(R.id.DepartureDaySpinner)
        tvSeat = findViewById<TextView>(R.id.tVSeat)
        spinner1 = findViewById<Spinner>(R.id.SpinnerFrom)
        spinner2 = findViewById<Spinner>(R.id.SpinnerTo)
        tv_search.text = destinationpoint + " -> " + startingpoint
        DepartureDaytV.text = myIntent.getStringExtra("ReturnTime").toString()
        tvSeat.text = myIntent.getStringExtra("Seat").toString()
        spinner1.onItemSelectedListener = this
        spinner2.onItemSelectedListener = this
        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        RecyclerViewBusTicket = findViewById<RecyclerView>(R.id.RecyclerViewBusTicket)
        val reentrantLock = ReentrantLock()
        reentrantLock.lock()
        dataList = ArrayList<BusTicket>()
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("XeKhach")
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val dataModel:BusTicket? = snapshot.getValue(BusTicket::class.java)
                    val itemId:String = snapshot.key.toString()
                    dataModel!!.setID(itemId)
                    var departureDateList:List<String> = dataModel!!.DepartureDate.split("\\s+".toRegex())
                    val indexToDrop = listOf(2,4,5)
                    val resultdepartureDate = departureDateList.filterIndexed { index, _ -> index in indexToDrop }
                    val resultdepartureDateString = resultdepartureDate.joinToString(separator = "-")
                    if(resultdepartureDateString == myIntent.getStringExtra("ReturnTime").toString())
                        if (dataModel.ArrivalPlace == startingpoint && dataModel.DeparturePlace == destinationpoint)
                        {
                            dataList.add(dataModel!!)
                        }
                }
                if (dataList.size == 0)
                {
                    progresssDialog.dismiss();
                    Toast.makeText(this@SelectBusTickets_2Activity, "Không tìm thấy chuyến đi phù hợp1", Toast.LENGTH_LONG).show()
                }
                else
                {
                    progresssDialog.dismiss();
                    var busTicketAdapter = BusTicketAdapter(dataList)
                    RecyclerViewBusTicket.adapter = busTicketAdapter
                    RecyclerViewBusTicket.layoutManager = LinearLayoutManager(this@SelectBusTickets_2Activity,LinearLayoutManager.VERTICAL,false)
                    val myExtraBoolean = intent.getBooleanExtra("RoundTrip", false)
                    busTicketAdapter.onItemClick = {selectedBusTicket ->
                        val selectedID:String = selectedBusTicket.getID()
                        Log.d("SecondID_Select2",selectedID)
                        val intent = Intent(this@SelectBusTickets_2Activity, PayActivity::class.java)
                        intent.putExtra("Seat", myIntent.getStringExtra("Seat").toString());
                        intent.putExtra("SecondSelectedID", selectedID);
                        intent.putExtra("FirstSelectedID", myIntent.getStringExtra("FirstSelectedID").toString());
                        intent.putExtra("RoundTrip", myExtraBoolean);
                        val LAUNCH_SECOND_ACTIVITY: Int = 1
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
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