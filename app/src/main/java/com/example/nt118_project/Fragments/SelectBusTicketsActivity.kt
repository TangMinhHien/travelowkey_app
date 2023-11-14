package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import org.w3c.dom.Text

class SelectBusTicketsActivity : AppCompatActivity() {
    private lateinit var tv_search: TextView
    private lateinit var RecyclerViewBusTicket: RecyclerView
    private lateinit var tvSeat: TextView
    private lateinit var DepartureDaytV: TextView
    private lateinit var dataList:ArrayList<BusTicket>
    private lateinit var f_dataList:ArrayList<BusTicket>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bus_tickets)

        val myIntent = intent // this is just for example purpose
        val startingpoint:String = myIntent.getStringExtra("Starting Point").toString()
        val destinationpoint:String = myIntent.getStringExtra("Destination Point").toString()
        val ReturnBtn = findViewById<ImageView>(R.id.iVBack)
        tv_search = findViewById<TextView>(R.id.tv_search)
        DepartureDaytV = findViewById<TextView>(R.id.DepartureDaySpinner)
        tvSeat = findViewById<TextView>(R.id.tVSeat)
        tv_search.text = startingpoint + " -> " + destinationpoint
        DepartureDaytV.text = myIntent.getStringExtra("DepartTime").toString()
        tvSeat.text = myIntent.getStringExtra("Seat").toString()
        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        RecyclerViewBusTicket = findViewById<RecyclerView>(R.id.RecyclerViewBusTicket)
        dataList = ArrayList<BusTicket>()
        f_dataList = ArrayList<BusTicket>()
        var firstBusTicket: BusTicket = BusTicket("Hoa Mai", "16", "10:00","13:00","3h","Văn Phòng 1","Văn phòng 2","TP. HCM","TP. Đà Nẵng","100.000")
        f_dataList.add(firstBusTicket)
        for (e in f_dataList)
        {
            if (e.ArrivalPlace == destinationpoint && e.DeparturePlace == startingpoint)
                dataList.add(e)
        }
        if (dataList.size == 0)
        {
            Toast.makeText(this, "Không tìm thấy chuyến đi phù hợp", Toast.LENGTH_LONG).show()
        }
        else
        {
            var busTicketAdapter = BusTicketAdapter(dataList)
            RecyclerViewBusTicket.adapter = busTicketAdapter
            RecyclerViewBusTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            val myExtraBoolean = intent.getBooleanExtra("RoundTrip", false)
            busTicketAdapter.onItemClick = {
                if(myExtraBoolean)
                {
                    val intent = Intent(this, SelectBusTickets_2Activity::class.java)
                    intent.putExtra("Starting Point", startingpoint);
                    intent.putExtra("Destination Point", destinationpoint);
                    intent.putExtra("DepartTime", DepartureDaytV.text.toString());
                    intent.putExtra("ReturnTime", myIntent.getStringExtra("ReturnTime").toString());
                    intent.putExtra("Seat", myIntent.getStringExtra("Seat").toString());
                    val LAUNCH_SECOND_ACTIVITY:Int = 1
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                }
                else
                {
                    val intent = Intent(this, PayActivity::class.java)
                    val LAUNCH_SECOND_ACTIVITY:Int = 1
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                }
            }
        }

    }
}