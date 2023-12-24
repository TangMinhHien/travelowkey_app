package com.example.nt118_project.flight

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import com.example.nt118_project.Fragments.Flight1Fragment
import com.example.nt118_project.Fragments.Flight2Fragment
import com.example.nt118_project.Fragments.FragmentPageAdapter
import com.example.nt118_project.R
import com.example.nt118_project.hotel.ListofHotelsActivity
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class SearchFlightActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private  lateinit var  viewPager2: ViewPager2
    private lateinit var adapter: SearchFlightAdapter
    private  lateinit var btn_back: ImageButton
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_flight)
        tabLayout = findViewById(R.id.search_flight_tab_layout)
        viewPager2 = findViewById(R.id.search_flight_viewpager2)
        adapter = SearchFlightAdapter(supportFragmentManager,lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Một chiều"))
        tabLayout.addTab(tabLayout.newTab().setText("Khứ hồi"))
        viewPager2.adapter = adapter
        btn_back = findViewById(R.id.search_flight_back)

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
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        val listFlightsBtn = findViewById<Button>(R.id.btn_search)
        listFlightsBtn.setOnClickListener{
            if (viewPager2.currentItem==0) {
                var currentFragment = supportFragmentManager.findFragmentByTag("f" + viewPager2.currentItem) as Flight1Fragment?
                if (currentFragment?.getValue()!=null){
                    var result = currentFragment?.getValue()
                    if (result?.getString("From")==result?.getString("To")){
                        Toast.makeText(this, "Vui lòng chọn điểm xuất phát và điểm đến khác nhau", Toast.LENGTH_LONG).show()}
                    else {
                        val intent = Intent(this, ListofFlightsActivity::class.java)
                        intent.putExtras(currentFragment?.getValue()!!)
                        Log.d("bundle","${currentFragment.getValue()}")
                        val LAUNCH_SECOND_ACTIVITY:Int = 1
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                    }}
            }
            else {
                var currentFragment = supportFragmentManager.findFragmentByTag("f" + viewPager2.currentItem) as Flight2Fragment?
                if (currentFragment?.getValue()!=null){
                    var result = currentFragment?.getValue()
                    val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val dateStart = LocalDate.parse(result?.getString("Date"), sdf)
                    val dateReturn = LocalDate.parse(result?.getString("ReturnDate"), sdf)
                    val compareTo = dateStart.compareTo(dateReturn)
                    if (result?.getString("From")==result?.getString("To")){
                        Toast.makeText(this, "Vui lòng chọn điểm xuất phát và điểm đến khác nhau", Toast.LENGTH_LONG).show()}
                    else if (compareTo>0){
                        Toast.makeText(this, "Vui lòng chọn ngày khởi hành nhỏ hơn hoặc bằng ngày về", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this, ListofFlightsActivity::class.java)
                        intent.putExtras(currentFragment?.getValue()!!)
                        val LAUNCH_SECOND_ACTIVITY:Int = 1
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                    }}
            }


        }
        btn_back.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}