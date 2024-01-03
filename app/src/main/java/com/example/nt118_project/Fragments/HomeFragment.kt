package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.nt118_project.Model.Notification
import com.example.nt118_project.R
import com.example.nt118_project.flight.SearchFlightActivity
import com.example.nt118_project.hotel.SearchHotelActivity
import com.example.nt118_project.notification.ListofNotificationsActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private  lateinit var  viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private lateinit var dataList:ArrayList<Notification>
    private lateinit var dbRef: DatabaseReference
    private lateinit var new_notify: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_notify = view.findViewById(R.id.new_notify)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager2 = view.findViewById(R.id.viewpaper2)
        adapter = FragmentPageAdapter(childFragmentManager,lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Khuyến mãi"))
        tabLayout.addTab(tabLayout.newTab().setText("Dành cho bạn"))
        dataList = ArrayList<Notification>()

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!=null){
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid as String
        dbRef = FirebaseDatabase.getInstance().getReference("Notification")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()){
                    for (Snap in snapshot.children){
                        val data = Snap.getValue(Notification::class.java)
                        if (data!!.User_Id == uid && data!!.State!="Seen")
                        {dataList.add(data!!)}
                    }
                    if (dataList.size>0)
                    {
                        new_notify.visibility = View.VISIBLE
                    }
                    else{
                        new_notify.visibility =  View.GONE
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val BusTicketBtn = view.findViewById<ImageButton>(R.id.img_bus)
        BusTicketBtn.setOnClickListener {
            val intent = Intent(activity, BookBusTicketsActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val searchFlyBtn = view.findViewById<ImageButton>(R.id.img_airplane)
        searchFlyBtn.setOnClickListener {
            val intent = Intent(activity, SearchFlightActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val searchHotelBtn = view.findViewById<ImageButton>(R.id.img_hotel)
        searchHotelBtn.setOnClickListener {
            val intent = Intent(activity, SearchHotelActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val NotificationBtn = view.findViewById<ImageButton>(R.id.logo_notification)
        NotificationBtn.setOnClickListener{
            val intent = Intent(activity, ListofNotificationsActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
    }

}