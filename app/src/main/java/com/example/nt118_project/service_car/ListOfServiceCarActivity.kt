package com.example.nt118_project.service_car

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ListOfServiceCarActivity: AppCompatActivity () {
    private lateinit var tv_info_servicecar_1: TextView
    private lateinit var tv_info_servicecar_2: TextView
    private lateinit var RecyclerViewServiceCarTicket: RecyclerView
    private lateinit var tv_info_sup: TextView
    private lateinit var dataList:ArrayList<ServiceCar_Ticket>
    private lateinit var DepartureDay: String
    private lateinit var ReturnDay: String
    private lateinit var db: FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var progresssDialog: ProgressDialog
    private lateinit var sorting_btn: FloatingActionButton
    private lateinit var tv_notfound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_servicecar)

        val myIntent = intent
        val value: Bundle = myIntent.getExtras()!!
        val max_require = value.getString("NumSeat")!![0].digitToInt()
        Log.d("max_require","${max_require}")
        tv_info_servicecar_1 = findViewById(R.id.info_servicecar_1)
        tv_info_servicecar_2 = findViewById(R.id.info_servicecar_2)

        if (value.getBoolean("is_return")) {
            tv_info_servicecar_1.text = "Xe ở" + value.getString("Place")
            tv_info_servicecar_2.text = value.getString("Date Depature") + "-" + value.getString("Date End")
            tv_info_sup.text = value.getString("ReturnDate") + " - " + value.getString("NumSeat") + " vé - " + value.getString("SeatClass")
            tv_info_sup.isSelected = true
        }
        else {
            tv_info_servicecar_1.text = "Xe ở" + value.getString("Place")
            tv_info_servicecar_2.text = value.getString("Date Depature") + "-" + value.getString("Date End")
            tv_info_sup.text = value.getString("Date") + " - " + value.getString("NumSeat") + " vé - " + value.getString("SeatClass")
        }

        val backBtn = findViewById<ImageButton>(R.id.list_servicecar_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        RecyclerViewServiceCarTicket = findViewById<RecyclerView>(R.id.sv_list_servicecar)
        dataList = ArrayList<ServiceCar_Ticket>()

        progresssDialog = ProgressDialog(this@ListOfServiceCarActivity)
        progresssDialog.setMessage("Đang tải dữ liệu...")
        progresssDialog.show()

        db = Firebase.firestore
        ref = db.collection("ServiceCar")
        if (!value.getBoolean("return_check")){
            ref.whereEqualTo("Date",value.getString("Date")).whereEqualTo("From",value.getString("From")).whereEqualTo("To",value.getString("To"))
                .whereEqualTo("SeatClass",value.getString("SeatClass"))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.toObject <ServiceCar_Ticket>()

//                        data.setID(document.id)
                        if (data.NumSeat>=max_require){
                            dataList.add(data)
                        }
                    }
                    if (dataList.size==0){
                        progresssDialog.dismiss();
                        tv_notfound.visibility = View.VISIBLE
                    }
                    else {
                        progresssDialog.dismiss()
                        var ServiceCarTicketAdapter = ServiceCar_Ticket_Adapter(dataList,this@ListOfServiceCarActivity)
                        RecyclerViewServiceCarTicket.adapter = ServiceCarTicketAdapter
                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL, false
                        )
                        ServiceCarTicketAdapter.onItemClick = { selectedFlightTicket ->
                            val selectedID: String = selectedFlightTicket.id
                            val intent = Intent(this@ListOfServiceCarActivity,PayActivity::class.java)
                            intent.putExtra("FirstSelectedID", selectedID)
                            intent.putExtra("SecondSelectedID", "")
                            intent.putExtra("Seat",value.getString("NumSeat"))
                            intent.putExtra("Tag", "Flight");
                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                        sorting_btn.setOnClickListener {
                            val popupMenu = PopupMenu(this, it)
                            popupMenu.inflate(R.menu.menu_sorting)
                            var newDataOfRecyclerView:List<ServiceCar_Ticket>
                            newDataOfRecyclerView = dataList
                            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                                when (item.itemId) {
                                    R.id.descending_sort -> {
                                        var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                        val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                        flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                            val selectedID: String = selectedFlightTicket.id
                                            val intent = Intent(this@ListOfServiceCarActivity,
                                                PayActivity::class.java)
                                            intent.putExtra("FirstSelectedID", selectedID)
                                            intent.putExtra("SecondSelectedID", "")
                                            intent.putExtra("Seat",value.getString("NumSeat"))
                                            intent.putExtra("Tag", "Flight");
                                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                        true
                                    }
                                    R.id.ascending_sort -> {
                                        var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                        val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                        flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                            val selectedID: String = selectedFlightTicket.id
                                            val intent = Intent(this@ListOfServiceCarActivity,PayActivity::class.java)
                                            intent.putExtra("FirstSelectedID", selectedID)
                                            intent.putExtra("SecondSelectedID", "")
                                            intent.putExtra("Seat",value.getString("NumSeat"))
                                            intent.putExtra("Tag", "Flight");
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
                            val data = document.toObject<ServiceCar_Ticket>()
//                            data.setID(document.id)
                            if (data.NumSeat>=max_require){
                                dataList.add(data)
                            }
                        }
                        if (dataList.size==0){
                            progresssDialog.dismiss();
                            tv_notfound.visibility = View.VISIBLE
                        }
                        else {
                            progresssDialog.dismiss();
                            var flightTicketAdapter = ServiceCar_Ticket_Adapter(dataList,this@ListOfServiceCarActivity)
                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL, false
                            )
                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                val selectedID: String = selectedFlightTicket.id
                                var intent = Intent(this@ListOfServiceCarActivity,ListOfServiceCarActivity::class.java)
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

                            sorting_btn.setOnClickListener {
                                val popupMenu = PopupMenu(this, it)
                                popupMenu.inflate(R.menu.menu_sorting)
                                var newDataOfRecyclerView:List<ServiceCar_Ticket>
                                newDataOfRecyclerView = dataList
                                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                                    when (item.itemId) {
                                        R.id.descending_sort -> {
                                            var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                            val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.id
                                                var intent = Intent(this@ListOfServiceCarActivity,ListOfServiceCarActivity::class.java)
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
                                            true
                                        }
                                        R.id.ascending_sort -> {
                                            var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                            val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.id
                                                var intent = Intent(this@ListOfServiceCarActivity,ListOfServiceCarActivity::class.java)
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
                                            true
                                        }
                                        else -> false
                                    }
                                }
                                popupMenu.show()
                            }
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
                            val data = document.toObject<ServiceCar_Ticket>()
//                            data.setID(document.id)
                            if (data.NumSeat>=max_require){
                                dataList.add(data)
                            }
                        }
                        if (dataList.size==0){
                            progresssDialog.dismiss();
                            tv_notfound.visibility = View.VISIBLE
                        }
                        else {
                            progresssDialog.dismiss();
                            var flightTicketAdapter = ServiceCar_Ticket_Adapter(dataList,this@ListOfServiceCarActivity)
                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL, false
                            )
                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                val selectedID: String = selectedFlightTicket.id
                                var intent = Intent(this@ListOfServiceCarActivity,PayActivity::class.java)
                                intent.putExtra("FirstSelectedID", value.getString("FirstSelectedID"))
                                intent.putExtra("SecondSelectedID", selectedID)
                                intent.putExtra("Seat",value.getString("NumSeat"))
                                intent.putExtra("Tag", "Flight")
                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                            sorting_btn.setOnClickListener {
                                val popupMenu = PopupMenu(this, it)
                                popupMenu.inflate(R.menu.menu_sorting)
                                var newDataOfRecyclerView:List<ServiceCar_Ticket>
                                newDataOfRecyclerView = dataList
                                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                                    when (item.itemId) {
                                        R.id.descending_sort -> {
                                            var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                            val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.id
                                                var intent = Intent(this@ListOfServiceCarActivity,PayActivity::class.java)
                                                intent.putExtra("FirstSelectedID", value.getString("FirstSelectedID"))
                                                intent.putExtra("SecondSelectedID", selectedID)
                                                intent.putExtra("Seat",value.getString("NumSeat"))
                                                intent.putExtra("Tag", "Flight")
                                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                                            true
                                        }
                                        R.id.ascending_sort -> {
                                            var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                            val flightTicketAdapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_,this@ListOfServiceCarActivity)
                                            RecyclerViewServiceCarTicket.adapter = flightTicketAdapter
                                            RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                                            flightTicketAdapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.id
                                                var intent = Intent(this@ListOfServiceCarActivity,PayActivity::class.java)
                                                intent.putExtra("FirstSelectedID", value.getString("FirstSelectedID"))
                                                intent.putExtra("SecondSelectedID", selectedID)
                                                intent.putExtra("Seat",value.getString("NumSeat"))
                                                intent.putExtra("Tag", "Flight")
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
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Error", "Error getting documents: ", exception)
                    }
            }
        }
    }
}