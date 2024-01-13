package com.example.nt118_project.service_car

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nt118_project.Fragments.ServiceCar1_Fragment
import com.example.nt118_project.Fragments.ServiceCar2_Fragment
import com.example.nt118_project.Model.ServiceCar_Ticket
import com.example.nt118_project.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.type.DateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


open class Search_ServiceCar_Activity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
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
    private lateinit var dataList: ArrayList<String>
    private lateinit var dataList2: ArrayList<String>
    private lateinit var dataList3: ArrayList<ServiceCar_Ticket>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_servicecar)

        tabLayout = findViewById(R.id.servicecar_tab_layout)
        btn_back = findViewById(R.id.search_servicecar_back)
        viewPager2 = findViewById(R.id.servicecar_search_viewpager2)
        adapter = Search_ServiceCar_Adapter (supportFragmentManager, lifecycle)
        dataList = ArrayList<String>()
        dataList2 = ArrayList<String>()
        dataList3 = ArrayList<ServiceCar_Ticket>()

        progresssDialog = ProgressDialog(this@Search_ServiceCar_Activity);
        progresssDialog.setMessage("Đang tải dữ liệu...");
        progresssDialog.show();

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid as String
            db = Firebase.firestore
            ref = db.collection("Invoice")
            ref.whereEqualTo("tag","ServiceCar").whereEqualTo("user_Id",uid).limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data = document.id
                        dataList.add(data)
                    }
                    if (dataList.size>0) {
                        progresssDialog.dismiss()
//                        ref2 = db.collection("ServiceCar_invoice")
//                        ref2.whereIn("invoice_Id", dataList)
//                            .get()
//                            .addOnSuccessListener { documents ->
//                                for (document in documents) {
//                                    val data2 = document["id_ticket"] as String
//                                    dataList2.add(data2)
//                                }
//                                ref3 = db.collection("ServiceCar")
//                                ref3.whereIn("Id", dataList2)
//                                    .get()
//                                    .addOnSuccessListener { documents ->
//                                        for (document in documents) {
//                                            val data3 = document.toObject<ServiceCar_Ticket>()
//                                            dataList3.add(data3)
//                                        }
//                                        progresssDialog.dismiss()
//                                        var recentServiceCarAdapter = RecentServiceCarTicketAdapter(
//                                            dataList3,
//                                            this@Search_ServiceCar_Activity
//                                        )
//                                        RecyclerviewRecentFlightTicket.adapter =
//                                            recentServiceCarAdapter
//                                        RecyclerviewRecentFlightTicket.layoutManager =
//                                            LinearLayoutManager(
//                                                this,
//                                                LinearLayoutManager.HORIZONTAL, false
//                                            )
//                                        recentServiceCarAdapter.onItemClick = { selectedServiceCarTicket ->
//                                            val intent = Intent(this@Search_ServiceCar_Activity, ListOfServiceCarActivity::class.java)
//                                            val currentDate = LocalDate.now()
//                                            val currentDay = currentDate.dayOfMonth
//                                            val currentMonth = currentDate.monthValue
//                                            val currentYear = currentDate.year
//                                            val Date = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
//                                            var value = Bundle()
//                                            value.putBoolean("return_check",false)
//                                            value.putBoolean("is_return",false)
//                                            value.putString("Date",Date)
//                                            value.putString("CarName",selectedServiceCarTicket.CarName)
//                                            value.putString("Place",selectedServiceCarTicket.Place)
//                                            value.putString("NumSeat",selectedServiceCarTicket.NumSeat.toString())
//                                            value.putString("NumLuggage",selectedServiceCarTicket.NumLuggage.toString())
//                                            intent.putExtras(value)
//                                            val LAUNCH_SECOND_ACTIVITY: Int = 1
//                                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
//                                    }
//                            }
                    }
                    else{
                        progresssDialog.dismiss()
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
                if (currentFragment?.getValue()!=null){
                    var result = currentFragment?.getValue()

                    val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")
                    val getDateDepart = result?.getString("DateDepature").toString()
                    val getTimeDepart = result?.getString("Time").toString()

                    val timeDepart = getDateDepart + " " + getTimeDepart

//                    val currentTime = LocalTime.now ()
//                    val currentHour = currentTime.hour
//                    val currentMinute = currentTime.minute
//                    val rightNow = Calendar.getInstance()
//                    val currentHour: Int = rightNow.get(Calendar.HOUR) // return the hour in 12 hrs format (ranging from 0-11)
//                    val currentMinute: Int = rightNow.get(Calendar.MINUTE)
//                    val time = LocalTime.parse(currentHour.toString() + ":" + currentMinute.toString(), DateTimeFormatter.ofPattern("H:mm"))
//                    val currHour  = time.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
//
//                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//                    val currentDate = LocalDate.now().format(formatter)
//                    val currDate = currentDate
//
//                    val dateDepart = LocalDateTime.parse(timeDepart, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
//                    val dateReturn = LocalDateTime.parse(currDate+" "+currHour, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
//                    var compareTo:Int = dateReturn.compareTo(dateDepart)

                    var compareTo = -1

                    if (result?.getString("Place") == null) {
                        Toast.makeText(this, "Vui lòng chọn địa điểm đón khách", Toast.LENGTH_LONG).show()
                    }
                    else if(compareTo>0)
                    {
                        Toast.makeText(this, "Vui lòng nhập thời gian đón khách sau thời điểm hiện tại", Toast.LENGTH_LONG).show()
                    }
                    else if(result?.getString("Time") == ""){
                        Toast.makeText(this, "Vui lòng nhập thời điểm đón khách", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this, ListOfServiceCarActivity::class.java)
                        intent.putExtras(currentFragment?.getValue()!!)
                        Log.d("bundle", "${currentFragment.getValue()}")
                        val LAUNCH_SECOND_ACTIVITY: Int = 1
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                    }
                }
            }
            else {
                var currentFragment = supportFragmentManager.findFragmentByTag("f" + viewPager2.currentItem) as ServiceCar2_Fragment?
                if (currentFragment?.getValue()!=null){
                    var result = currentFragment?.getValue()

                    val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")
                    val getDateDepart = result?.getString("DateDepature").toString()
                    val getTimeDepart = result?.getString("TimeDepature").toString()
                    val getDateEnd = result?.getString("DateEnd").toString()
                    val getTimeEnd = result?.getString("TimeEnd").toString()

                    val timeDepart = getDateDepart + " " + getTimeDepart
                    val timeEnd = getDateEnd + " " + getTimeEnd

                    val dateDepart = LocalDateTime.parse(timeDepart, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
//                    val dateDepart = LocalDate.parse(timeDepart, sdf)
//                    val dateReturn = LocalDate.parse(timeEnd, sdf)
                    val dateReturn = LocalDateTime.parse(timeEnd, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                    val compareTo = dateReturn.compareTo(dateDepart)

                    if (result?.getString("Place") == null) {
                        Toast.makeText(this, "Vui lòng chọn địa điểm đón khách", Toast.LENGTH_LONG).show()
                    }
                    else if (compareTo<=0){
                        Toast.makeText(this, "Vui lòng chọn ngày khởi hành trước ngày về", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this, ListOfServiceCarActivity::class.java)
                        intent.putExtras(currentFragment?.getValue()!!)
                        Log.d("bundle","${currentFragment.getValue()}")
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Selected","Select Seat")
        (view as TextView).setTextColor(Color.WHITE)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("Selected","Not select seat")
    }
}