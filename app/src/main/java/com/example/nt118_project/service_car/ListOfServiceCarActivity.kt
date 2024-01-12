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

class ListOfServiceCarActivity : AppCompatActivity() {
    private lateinit var tv_info_servicecar_1: TextView
    private lateinit var tv_info_servicecar_2: TextView
    private lateinit var RecyclerViewServiceCarTicket: RecyclerView
    private lateinit var dataList: ArrayList<ServiceCar_Ticket>
    private lateinit var db: FirebaseFirestore
    private lateinit var ref1: CollectionReference
    private lateinit var ref2: CollectionReference
    private lateinit var progresssDialog: ProgressDialog
    private lateinit var sorting_btn: FloatingActionButton
    private lateinit var tv_notfound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_servicecar)

        val myIntent = intent
        val value: Bundle = myIntent.getExtras()!!
        tv_info_servicecar_1 = findViewById(R.id.info_servicecar_1)
        tv_info_servicecar_2 = findViewById(R.id.info_servicecar_2)

        if (value.getBoolean("is_return")) {
            tv_info_servicecar_1.text = "Xe ở " + value.getString("Place")
            tv_info_servicecar_2.text = value.getString("DateDepature") + "đến" + value.getString("DateEnd")
        } else {
            tv_info_servicecar_1.text = "Xe ở " + value.getString("Place")
            tv_info_servicecar_2.text = value.getString("DateDepature") + "đến" + value.getString("DateEnd")
        }

        val backBtn = findViewById<ImageButton>(R.id.list_servicecar_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        sorting_btn = findViewById(R.id.sorting_button)
        tv_notfound = findViewById(R.id.result_not_found)
        RecyclerViewServiceCarTicket = findViewById<RecyclerView>(R.id.sv_list_servicecar)
        dataList = ArrayList<ServiceCar_Ticket>()

        progresssDialog = ProgressDialog(this@ListOfServiceCarActivity)
        progresssDialog.setMessage("Đang tải dữ liệu...")
        progresssDialog.show()

        db = Firebase.firestore
        if (!value.getBoolean("return_check")) {
            ref1 = db.collection("ServiceCar_NoDriver")
            ref1.whereEqualTo("Place", value.getString("Place")).whereEqualTo("Status", "Free")
                .whereEqualTo("type", "Có tài xế")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        try{
                            val Data = document.toObject<ServiceCar_Ticket>()
                            dataList.add(Data)
                        }
                        catch (e:Exception){
                            Log.d("Fail","Can't get data")
                        }
                    }
                    if (dataList.size == 0) {
                        progresssDialog.dismiss()
                        tv_notfound.visibility = View.VISIBLE
                    } else {
                        progresssDialog.dismiss()
                        var ServiceCarTicketAdapter = ServiceCar_Ticket_Adapter(dataList, this@ListOfServiceCarActivity)
                        RecyclerViewServiceCarTicket.adapter = ServiceCarTicketAdapter
                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        ServiceCarTicketAdapter.onItemClick = { selectedServiceCarTicket ->
                            val selectedID: String = selectedServiceCarTicket.Id
                            val intent = Intent(this@ListOfServiceCarActivity, PayActivity::class.java)
                            intent.putExtra("FirstSelectedID", selectedID)
                            intent.putExtra("Place",value.getString("Place"))
                            intent.putExtra("Duration", value.getString("Duration"))
                            intent.putExtra("DateStart", value.getString("DateDepature"))
                            intent.putExtra("TimeStart", value.getString ("Time"))
                            intent.putExtra("DateEnd", value.getString("DateEnd"))
                            intent.putExtra("Tag", "ServiceCar")
                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                        }

                        sorting_btn.setOnClickListener {
                            val popupMenu = PopupMenu(this, it)
                            popupMenu.inflate(R.menu.menu_sorting)
                            var newDataOfRecyclerView: List<ServiceCar_Ticket>
                            newDataOfRecyclerView = dataList
                            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                                when (item.itemId) {
                                    R.id.descending_sort -> {
                                        var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                        val servicecar_ticket_Adapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_, this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter = servicecar_ticket_Adapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                        servicecar_ticket_Adapter.onItemClick = { selected_ServiceCar_Ticket ->
                                                val selectedID: String = selected_ServiceCar_Ticket.Id
                                                val intent = Intent(this@ListOfServiceCarActivity, PayActivity::class.java)
                                                intent.putExtra("FirstSelectedID", selectedID)
                                                intent.putExtra("Place",value.getString("Place"))
                                                intent.putExtra("Duration", value.getString("Duration"))
                                                intent.putExtra("DateStart", value.getString("DateDepature"))
                                                intent.putExtra("TimeStart", value.getString ("Time"))
                                                intent.putExtra("DateEnd", value.getString("DateEnd"))
                                                intent.putExtra("Tag", "ServiceCar")
                                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                            }
                                        true
                                    }

                                    R.id.ascending_sort -> {
                                        var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                        val servicecar_ticket_Adapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_, this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter = servicecar_ticket_Adapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                        servicecar_ticket_Adapter.onItemClick = { selected_ServiceCar_Ticket ->
                                                val selectedID: String = selected_ServiceCar_Ticket.Id
                                                val intent = Intent(this@ListOfServiceCarActivity, PayActivity::class.java)
                                                intent.putExtra("FirstSelectedID", selectedID)
                                                intent.putExtra("Place",value.getString("Place"))
                                                intent.putExtra("Duration", value.getString("Duration"))
                                                intent.putExtra("DateStart", value.getString("DateDepature"))
                                                intent.putExtra("TimeStart", value.getString ("Time"))
                                                intent.putExtra("DateEnd", value.getString("DateEnd"))
                                                intent.putExtra("Tag", "ServiceCar")
                                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                            }
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
                    Log.w("Error", "Error getting documents: ", exception) }
        } else {
            ref2 = db.collection("ServiceCar_Driver")
            ref2.whereEqualTo("Place", value.getString("Place")).whereEqualTo("Status", "Free")
                .whereEqualTo("type", "Tự lái")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        try{
                            val Data = document.toObject<ServiceCar_Ticket>()
                            dataList.add(Data)
                        }
                        catch (e:Exception){
                            Log.d("Fail","Can't get data")
                        }
                    }
                    if (dataList.size == 0) {
                        progresssDialog.dismiss();
                        tv_notfound.visibility = View.VISIBLE
                    } else {
                        progresssDialog.dismiss();
                        var ServiceCarTicketAdapter = ServiceCar_Ticket_Adapter(dataList, this@ListOfServiceCarActivity)
                        RecyclerViewServiceCarTicket.adapter = ServiceCarTicketAdapter
                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this@ListOfServiceCarActivity, LinearLayoutManager.VERTICAL, false)
                        ServiceCarTicketAdapter.onItemClick = { selected_ServiceCar_Ticket ->
                            val selectedID: String = selected_ServiceCar_Ticket.Id
                            var intent = Intent(this@ListOfServiceCarActivity, ListOfServiceCarActivity::class.java)
                            intent.putExtra("FirstSelectedID", selectedID)
                            intent.putExtra("return_check", true)
                            intent.putExtra("Place",value.getString("Place"))
                            intent.putExtra("Duration", value.getString("Duration"))
                            intent.putExtra("DateStart", value.getString("DateDepature"))
                            intent.putExtra("TimeStart", value.getString ("TimeStart"))
                            intent.putExtra("DateEnd", value.getString("DateEnd"))
                            intent.putExtra("TimeEnd", value.getString ("TimeEnd"))
                            intent.putExtra("Tag", "ServiceCar")
                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                        }

                        sorting_btn.setOnClickListener {
                            val popupMenu = PopupMenu(this, it)
                            popupMenu.inflate(R.menu.menu_sorting)
                            var newDataOfRecyclerView: List<ServiceCar_Ticket>
                            newDataOfRecyclerView = dataList
                            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                                when (item.itemId) { R.id.descending_sort -> {
                                        var newDataOfRecyclerView_ = newDataOfRecyclerView.sortedByDescending { it.Price }.toCollection(ArrayList())
                                        val servicecar_ticket_Adapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_, this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter = servicecar_ticket_Adapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                        servicecar_ticket_Adapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.Id
                                                var intent = Intent(this@ListOfServiceCarActivity, ListOfServiceCarActivity::class.java)
                                                intent.putExtra("FirstSelectedID", selectedID)
                                                intent.putExtra("return_check", true)
                                                intent.putExtra("Place",value.getString("Place"))
                                                intent.putExtra("Duration", value.getString("Duration"))
                                                intent.putExtra("DateStart", value.getString("DateDepature"))
                                                intent.putExtra("TimeStart", value.getString ("TimeStart"))
                                                intent.putExtra("DateEnd", value.getString("DateEnd"))
                                                intent.putExtra("TimeEnd", value.getString ("TimeEnd"))
                                                intent.putExtra("Tag", "ServiceCar")
                                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                            }
                                        true
                                    }

                                    R.id.ascending_sort -> {
                                        var newDataOfRecyclerView_ =
                                            newDataOfRecyclerView.sortedBy { it.Price }.toCollection(ArrayList())
                                        val servicecar_ticket_Adapter = ServiceCar_Ticket_Adapter(newDataOfRecyclerView_, this@ListOfServiceCarActivity)
                                        RecyclerViewServiceCarTicket.adapter =
                                            servicecar_ticket_Adapter
                                        RecyclerViewServiceCarTicket.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                        servicecar_ticket_Adapter.onItemClick = { selectedFlightTicket ->
                                                val selectedID: String = selectedFlightTicket.Id
                                                var intent = Intent(this@ListOfServiceCarActivity, ListOfServiceCarActivity::class.java)
                                                intent.putExtra("FirstSelectedID", selectedID)
                                                intent.putExtra("return_check", true)
                                                intent.putExtra("Place",value.getString("Place"))
                                                intent.putExtra("Duration", value.getString("Duration"))
                                                intent.putExtra("DateStart", value.getString("DateDepature"))
                                                intent.putExtra("TimeStart", value.getString ("TimeStart"))
                                                intent.putExtra("DateEnd", value.getString("DateEnd"))
                                                intent.putExtra("TimeEnd", value.getString ("TimeEnd"))
                                                intent.putExtra("Tag", "ServiceCar")
                                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                            }
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