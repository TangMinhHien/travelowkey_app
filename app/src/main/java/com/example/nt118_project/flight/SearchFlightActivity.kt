package com.example.nt118_project.flight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import com.example.nt118_project.Fragments.Flight1Fragment
import com.example.nt118_project.Fragments.Flight2Fragment
import com.example.nt118_project.Fragments.FragmentPageAdapter
import com.example.nt118_project.R
import com.google.android.material.tabs.TabLayout

open class SearchFlightActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private  lateinit var  viewPager2: ViewPager2
    private lateinit var adapter: SearchFlightAdapter
    private  lateinit var btn_back: ImageButton
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
                Toast.makeText(this@SearchFlightActivity,"tabchanged",Toast.LENGTH_LONG)
            }
        })

        btn_back.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}