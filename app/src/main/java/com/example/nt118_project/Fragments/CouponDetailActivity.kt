package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.CouponAdapter
import com.example.nt118_project.Adapter.RecentFlightTicketAdapter
import com.example.nt118_project.Model.Coupon
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Point
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class UsedCoupon {
    public lateinit var id: String
    public lateinit var user_Id: String
    public lateinit var coupon_Id: ArrayList<String>
    constructor(){}
    constructor(coupon_Id:ArrayList<String>, id:String, user_Id:String)
    {
        this.id=id
        this.user_Id=user_Id
        this.coupon_Id=coupon_Id
    }
}
class CouponDetailActivity : AppCompatActivity() {
    private lateinit var RecyclerViewCoupon: RecyclerView
    private lateinit var RecyclerViewUsedCoupon: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var ref: CollectionReference
    private lateinit var ref_2: CollectionReference
    private lateinit var dataList:ArrayList<Coupon>
    private lateinit var dataList_2:ArrayList<Coupon>
    private lateinit var listUsedCoupon: ArrayList<String>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_detail)
        dataList = ArrayList<Coupon>()
        dataList_2 = ArrayList<Coupon>()
        listUsedCoupon = ArrayList()

        val BackUser = findViewById<ImageView>(R.id.iVBack)

        BackUser.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        RecyclerViewCoupon = findViewById(R.id.RecyclerviewCoupon)
        RecyclerViewUsedCoupon = findViewById(R.id.RecyclerviewUsedCoupon)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val strcurrentDay = LocalDateTime.now().format(formatter)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDay: Date = sdf.parse(strcurrentDay)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid

        db = Firebase.firestore

        ref_2 = db.collection("UsedCoupon")

        ref_2.whereEqualTo("user_Id", user_id).get().addOnSuccessListener { documents ->
            for (document in documents)
            {
                val usedcoupon_ = document.toObject(UsedCoupon::class.java)
                Log.d("usedcoupon_", usedcoupon_.coupon_Id[0])
                listUsedCoupon = usedcoupon_.coupon_Id
                break
            }
            ref = db.collection("Coupon")
            ref.get().addOnSuccessListener { documents ->
                for (document in documents)
                {
                    val coupon_ = document.toObject(Coupon::class.java)
                    if(currentDay.before(sdf.parse(coupon_.ExpiryDate)) && coupon_.Max > 0 && !(coupon_.id in listUsedCoupon))
                    {
                        dataList.add(coupon_)
                    }
                }
                if(dataList.size != 0)
                {
                    var CouponAdapter = CouponAdapter(dataList, this@CouponDetailActivity, false)
                    RecyclerViewCoupon.adapter = CouponAdapter
                    RecyclerViewCoupon.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, false
                    )
                }
            }
            if(listUsedCoupon.size != 0)
            {
                db.collection("Coupon").whereIn("id", listUsedCoupon).get().addOnSuccessListener {documents->
                    for (document in documents)
                    {
                        val coupon_ = document.toObject(Coupon::class.java)
                        dataList_2.add(coupon_)
                    }
                    var CouponAdapter = CouponAdapter(dataList_2, this@CouponDetailActivity, true)
                    RecyclerViewUsedCoupon.adapter = CouponAdapter
                    RecyclerViewUsedCoupon.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                }
            }
        }
    }

}