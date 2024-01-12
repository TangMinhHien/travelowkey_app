package com.example.nt118_project.Fragments

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.nt118_project.Adapter.FragmentPageHistoryPointAdapter
import com.example.nt118_project.Model.User
import com.example.nt118_project.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class PointDetailActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private  lateinit var  viewPager2: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_detail)

        val BackUser = findViewById<ImageView>(R.id.iVBack)
        BackUser.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        val ic_info:ImageView = findViewById(R.id.ic_info)

        ic_info.setOnClickListener {
            val builder = Dialog(this@PointDetailActivity)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
            builder.setTitle("Chi tiết hóa đơn")
            val view: View =
                LayoutInflater.from(this@PointDetailActivity).inflate(R.layout.point_info_dialog, null)
            builder.setContentView(view)
            builder.show()
        }

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid
        val usersRef = Firebase.firestore
        var user_ = User()
        val tVPoint:TextView = findViewById(R.id.tVPoint)
        usersRef.collection("User").document(user_id).get()
            .addOnSuccessListener { document ->
                if (document != null)
                {
                    user_ = document.toObject(User::class.java)!!
                    tVPoint.text = "Tổng điểm: "+user_.Point.toString()+" điểm"
                }
            }
            .addOnFailureListener{exception ->
                Log.e("TAG", "User data not found")
            }

        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewpaper2)
        var adapter_:FragmentPageHistoryPointAdapter = FragmentPageHistoryPointAdapter(supportFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả giao dịch"))
        tabLayout.addTab(tabLayout.newTab().setText("Tích được"))
        tabLayout.addTab(tabLayout.newTab().setText("Đã quy đổi"))
        viewPager2.adapter = adapter_

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
    }
}