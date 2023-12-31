package com.example.nt118_project.notification

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
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
    private lateinit var db: FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var progresssDialog: ProgressDialog
    private lateinit var recyclerviewNotification:RecyclerView
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_notifications)
        recyclerviewNotification = findViewById(R.id.RecyclerviewNotification)
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
        dbRef = FirebaseDatabase.getInstance().getReference("Notification")
        dbRef.orderByChild("State").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()){
                    for (Snap in snapshot.children){
                        val data = Snap.getValue(Notification::class.java)
                        dataList.add(data!!)
                    }
                    Log.d("check_data1","update failed ${dataList[0].Id} ${dataList[0].Text} ${dataList[0].Tag} ${dataList[0].State}")
                    var notificationAdapter = NotificationAdapter(dataList,this@ListofNotificationsActivity)
                    recyclerviewNotification.adapter = notificationAdapter
                    recyclerviewNotification.layoutManager = LinearLayoutManager(this@ListofNotificationsActivity,LinearLayoutManager.VERTICAL,false)
                    notificationAdapter.onItemClick= {selecteditem ->
                            val dbRef2 = FirebaseDatabase.getInstance().getReference("Notification")
                            dbRef2.child(selecteditem.Id).child("State").setValue("Seen")
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