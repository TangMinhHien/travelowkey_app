package com.example.nt118_project.hotel

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.HotelAdapter
import com.example.nt118_project.Adapter.RoomAdapter
import com.example.nt118_project.Fragments.PayActivity
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ListofRoomsActivity : AppCompatActivity() {
    private lateinit var tv_info_Room: TextView
    private lateinit var RecyclerViewRooms: RecyclerView
    private lateinit var tv_info_sup: TextView
    private lateinit var dataList:ArrayList<Room>
    private lateinit var hotel:Hotel
    private lateinit var db: FirebaseFirestore
    private lateinit var ref_hotel:DocumentReference
    private lateinit var ref: CollectionReference
    private lateinit var progresssDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listof_rooms)
        val myIntent = intent
        val value: Bundle = myIntent.getExtras()!!
        val max_require = value.getString("NumRoom")!![0].digitToInt()
        db = Firebase.firestore
//        Log.d("selected_id","${db.collection("Hotel").document(value.getString("SelectedID")!!).get().result.data}")
        ref_hotel = db.collection("Hotel").document(value.getString("SelectedID").toString())
        ref_hotel.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    hotel = document.toObject<Hotel>()!!
                    ref = db.collection("Room")

                    tv_info_Room = findViewById(R.id.info_room)
                    tv_info_Room.text = hotel.Name
                    tv_info_sup = findViewById(R.id.info_sup_room)
                    tv_info_sup.text = hotel.Address

                    RecyclerViewRooms = findViewById(R.id.RecyclerviewRoom)
                    dataList = ArrayList<Room>()
                    progresssDialog = ProgressDialog(this@ListofRoomsActivity);
                    progresssDialog.setMessage("Đang tải dữ liệu...");
                    progresssDialog.show();


                    ref.whereEqualTo("Hotel_id",hotel.Id).whereEqualTo("State","Free").get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            try{
                                Log.d("Room","${document.data}")
                                val Data = document.toObject<Room>()
                                if (Data.Max>=max_require){
                                dataList.add(Data)}
                            }
                            catch (e:Exception){
                                Log.d("Fail","Can't get data")
                            }
                        }
                        if (dataList.size == 0) {
                            progresssDialog.dismiss()
                            Toast.makeText(this, "Không tìm thấy phòng phù hợp", Toast.LENGTH_LONG).show()
                        }
                        else{
                            progresssDialog.dismiss()
                            var roomAdapter = RoomAdapter(dataList,this@ListofRoomsActivity)
                            RecyclerViewRooms.adapter = roomAdapter
                            RecyclerViewRooms.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL, false
                            )
                            roomAdapter.onItemClick = {selectedFlightTicket ->
                                val selectedID: String = selectedFlightTicket.Id
                                val intent = Intent(this@ListofRoomsActivity, PayActivity::class.java)
                                intent.putExtra("SelectedID", selectedID)
                                intent.putExtra("DayStart", value.getString("DayStart"));
                                intent.putExtra("DayEnd",value.getString("DayEnd"));
                                intent.putExtra("NumRoom", value.getString("NumRoom"));
                                val LAUNCH_SECOND_ACTIVITY: Int = 1
                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)}
                        }
                    }.addOnFailureListener { exception ->
                        Log.e("Firebase", "Error getting data: $exception")
                    }

                    val backBtn = findViewById<ImageButton>(R.id.list_rooms_back)
                    backBtn.setOnClickListener {
                        val returnIntent = Intent()
                        setResult(RESULT_CANCELED, returnIntent)
                        finish()
                    }
                }
            }
        }

    }
}