package com.example.nt118_project.service_car

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nt118_project.R
import com.google.android.material.tabs.TabLayout

class Search_ServiceCar_Activity: AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: Search_ServiceCar_Adapter
    private lateinit var btn_back: ImageButton
    private lateinit var progresssDialog: ProgressDialog
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

        val list_ServiceCar_btn = findViewById<Button>(R.id.btn_search)
        list_ServiceCar_btn.setOnClickListener{
            val intent = Intent(this, ListOfServiceCarActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }

        // Back Button
        btn_back.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}