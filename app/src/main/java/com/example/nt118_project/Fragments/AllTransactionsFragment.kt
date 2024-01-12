package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.AllTransactionsAdapter
import com.example.nt118_project.Adapter.NotificationAdapter
import com.example.nt118_project.MainActivity
import com.example.nt118_project.Model.Notification
import com.example.nt118_project.Model.Point
import com.example.nt118_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllTransactionsFragment : Fragment() {
    private lateinit var recyclerViewPoint: RecyclerView
    private lateinit var dbRef: DatabaseReference
    private lateinit var dataList:ArrayList<Point>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alltransactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewPoint = view.findViewById(R.id.RecyclerviewPoint)
        dataList = ArrayList<Point>()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Point")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()){
                    for (Snap in snapshot.children){
                        val data = Snap.getValue(Point::class.java)
                        if(data!!.User_Id == user_id && data!!.Type == "redeemed")
                        {
                            dataList.add(data!!)
                        }
                        else if (data!!.User_Id == user_id && data!!.Type!="redeemed"){
                            dataList.add(0,data!!)
                        }
                    }
                    if(dataList.size == 0)
                    {
                        recyclerViewPoint.setVisibility(View.GONE)
                    }
                    else
                    {
                        var notificationAdapter = AllTransactionsAdapter(dataList,requireActivity())
                        recyclerViewPoint.adapter = notificationAdapter
                        recyclerViewPoint.layoutManager = LinearLayoutManager(requireActivity(),
                            LinearLayoutManager.VERTICAL,false)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("check_data2","update failed")
            }
        }
        )
    }
}