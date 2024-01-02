package com.example.nt118_project.service_car

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.R
import com.example.nt118_project.Fragments.ServiceCar1_Fragment
import com.example.nt118_project.Fragments.ServiceCar2_Fragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class Search_ServiceCar_Activity: AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: Search_ServiceCar_Adapter
    private lateinit var btn_back: ImageButton
    private lateinit var progresssDialog: ProgressDialog
    private lateinit var RecyclerviewRecentFlightTicket: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var ref2: CollectionReference
    private lateinit var ref3: CollectionReference
    private lateinit var dataList:ArrayList<String>
    private lateinit var dataList2:ArrayList<String>
    private lateinit var dataList3:ArrayList<ServiceCar_Ticket>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_servicecar)

        tabLayout = findViewById(R.id.servicecar_tab_layout)
        btn_back = findViewById(R.id.search_servicecar_back)
        viewPager2 = findViewById(R.id.servicecar_search_viewpager2)
        adapter = Search_ServiceCar_Adapter (supportFragmentManager, lifecycle)

        progresssDialog = ProgressDialog(this@Search_ServiceCar_Activity);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid as String
            db = Firebase.firestore
            ref = db.collection("Invoice")
            ref.whereEqualTo("tag","Flight").whereEqualTo("user_Id",uid).limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.id
                        dataList.add(data)
                    }
                    if (dataList.size>0) {
                        ref2 = db.collection("Flight_invoice")
                        ref2.whereIn("invoice_Id", dataList)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val data2 = document["id_ticket_1"] as String
                                    dataList2.add(data2)
                                }
                                ref3 = db.collection("Flight")
                                ref3.whereIn("Id", dataList2)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val data3 = document.toObject<ServiceCar_Ticket>()
                                            dataList3.add(data3)
                                        }
                                        progresssDialog.dismiss()
                                        var recentFlightAdapter = RecentFlightTicketAdapter(
                                            dataList3,
                                            this@Search_ServiceCar_Activity
                                        )
                                        RecyclerviewRecentFlightTicket.adapter =
                                            recentFlightAdapter
                                        RecyclerviewRecentFlightTicket.layoutManager =
                                            LinearLayoutManager(
                                                this,
                                                LinearLayoutManager.HORIZONTAL, false
                                            )
                                        recentFlightAdapter.onItemClick = { selectedFlightTicket ->
                                            val intent = Intent(this@Search_ServiceCar_Activity,
                                                ListOfServiceCarActivity::class.java)
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
        } else {
        }

        // Set up TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Có tài xế"))
        tabLayout.addTab(tabLayout.newTab().setText("Tự lái"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // Set the viewPager2 adapter
        viewPager2.adapter = adapter
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
                Toast.makeText(this@Search_ServiceCar_Activity,"tabchanged", Toast.LENGTH_LONG)
            }
        })

        val listFlightsBtn = findViewById<Button>(R.id.btn_search)
        listFlightsBtn.setOnClickListener{
            if (viewPager2.currentItem == 0) {
                var currentFragment = supportFragmentManager.findFragmentByTag("f" + viewPager2.currentItem) as ServiceCar1_Fragment?
                if (currentFragment?.getValue() != null){
                    val intent = Intent(this, ListOfServiceCarActivity::class.java)
                    intent.putExtras (currentFragment?.getValue()!!)
                    Log.d("bundle","${currentFragment.getValue()}")
                    val LAUNCH_SECOND_ACTIVITY:Int = 1
                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                }
            }
            else {
                var currentFragment = supportFragmentManager.findFragmentByTag("f" + viewPager2.currentItem) as ServiceCar2_Fragment?
                if (currentFragment?.getValue()!=null){
                    var result = currentFragment?.getValue()

                    val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val dateStart = LocalDate.parse(result?.getString("Date"), sdf)
                    val dateReturn = LocalDate.parse(result?.getString("ReturnDate"), sdf)
                    val compareTo = dateStart.compareTo(dateReturn)
                    if (result?.getString("From")==result?.getString("To")){
                        Toast.makeText(this, "Vui lòng chọn điểm xuất phát và điểm đến khác nhau", Toast.LENGTH_LONG).show()}
                    else if (compareTo>=0){
                        Toast.makeText(this, "Vui lòng chọn ngày khởi hành trước ngày về", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this, ListOfServiceCarActivity::class.java)
                        var bundle: Bundle = Bundle ()
                        bundle.putString(currentFragment?.getValue()!!)
                        intent.putExtras(bundle)
                        val LAUNCH_SECOND_ACTIVITY:Int = 1
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                    }}
            }


        }

        // Back Button
        btn_back.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}