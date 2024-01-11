package com.example.nt118_project.notification

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.HotelAdapter
import com.example.nt118_project.Adapter.NotificationAdapter
import com.example.nt118_project.Fragments.BillFragment
import com.example.nt118_project.MainActivity
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Notification
import com.example.nt118_project.R
import com.example.nt118_project.hotel.ListofRoomsActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlin.reflect.typeOf

class ListofNotificationsActivity : AppCompatActivity() {
    private lateinit var dataList:ArrayList<Notification>
    private lateinit var recyclerviewNotification:RecyclerView
    private lateinit var tVNoti:TextView
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_notifications)
        recyclerviewNotification = findViewById(R.id.RecyclerviewNotification)
        tVNoti = findViewById(R.id.tVNoti)
        dataList = ArrayList<Notification>()
//        db = Firebase.firestore
//        ref = db.collection("Notification")
//        ref.orderBy("State",Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { documents->
//                for (document in documents){
//                    val data = document.toObject<Notification>()
//                    dataList.add(data)
//                }
//                Log.d("check_data","${dataList.toString()}")
//                var notificationAdapter = NotificationAdapter(dataList,this@ListofNotificationsActivity)
//                recyclerviewNotification.adapter = notificationAdapter
//                recyclerviewNotification.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//                notificationAdapter.onItemClick = {selectedItem ->
//
////                    val intent = Intent(this@ListofNotificationsActivity, BillFragment::class.java)
////                    val LAUNCH_SECOND_ACTIVITY: Int = 1
////                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
//                }
//            }
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Notification")
        dbRef.orderByChild("user_Id").equalTo(user_id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()){
                    var index_not_seen = 0
                    for (Snap in snapshot.children){
                        val data = Snap.getValue(Notification::class.java)
                        if(data!!.State=="Seen")
                        {
                            if (dataList.size-index_not_seen==0){
                                dataList.add(data)
                            }
                            else
                                for (i in index_not_seen..dataList.size){
                                    if (i==dataList.size){
                                        dataList.add(data!!)
                                    }
                                    else if (dataList[i].Created<data.Created){
                                        dataList.add(i,data!!)
                                        break
                                    }
                            }
                        }
                        else if (data!!.State!="Seen"){
                            if (index_not_seen==0){
                                dataList.add(0,data)
                            }
                            else
                                for (i in 0..index_not_seen){
                                    if (i == index_not_seen){
                                        dataList.add(index_not_seen,data!!)
                                    }
                                    else if (dataList[i].Created<data.Created){
                                        dataList.add(i,data!!)
                                        break
                                    }
                            }
                            index_not_seen+=1
                        }
                    }
                    if(dataList.size == 0)
                    {
                        recyclerviewNotification.setVisibility(View.GONE)
                    }
                    else
                    {
                        tVNoti.setVisibility(View.GONE)
                        var notificationAdapter = NotificationAdapter(dataList,this@ListofNotificationsActivity)
                        recyclerviewNotification.adapter = notificationAdapter
                        recyclerviewNotification.layoutManager = LinearLayoutManager(this@ListofNotificationsActivity,LinearLayoutManager.VERTICAL,false)
                        notificationAdapter.onItemClick= {selecteditem ->
                            val dbRef2 = FirebaseDatabase.getInstance().getReference("Notification")
                            dbRef2.child(selecteditem.Id).child("state").setValue("Seen")
                                .addOnCompleteListener {
                                    Log.d("check_data3","update successed")
                                }.addOnFailureListener { err ->
                                    Log.d("check_data4","update failed ${err.toString()}")
                                }
                            val intent = Intent(this@ListofNotificationsActivity, MainActivity::class.java)
                            intent.putExtra("previous_intent","notification")
                            val LAUNCH_SECOND_ACTIVITY: Int = 1
                            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("check_data2","update failed")
            }
        }
        )
       val backBtn = findViewById<ImageButton>(R.id.list_notifications_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
    }
}