package com.example.nt118_project.Fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.User
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
class user{
    public var Id:String = ""
    public var Email: String = ""
    public var PhoneNumber: String = ""
    public var UserName: String = ""
    public var Address: String = ""
    public var Sex: String = ""
    public var Name: String = ""
    public var Birthday: String = ""
    public var Point:Int = 0

    constructor()
    constructor(id:String, email:String, phone:String, userName: String, address:String, sex:String, name:String, bd:String, point:Int)
    {
        this.Id = id
        this.Email = email
        this.PhoneNumber = phone
        this.UserName = userName
        this.Address = address
        this.Sex = sex
        this.Name = name
        this.Birthday = bd
        this.Point = point
    }
}
class UserDetailFragment : AppCompatActivity() {
    public val myActivity: Activity? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_fragment)

        val userFullName: EditText = findViewById(R.id.eTFullName)
        val userName: TextView = findViewById(R.id.user_name)
        val userSex: EditText = findViewById(R.id.eTSex)
        val userPhoneNumber: EditText = findViewById(R.id.eTPhoneNumber)
        val userEmail: EditText = findViewById(R.id.eTEmail)
        val userBD: EditText = findViewById(R.id.eTBirthDay)
        val userAddress: EditText = findViewById(R.id.eTAddress)
        val tVChangePw: TextView = findViewById(R.id.tVChangePw)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser_: FirebaseUser? = firebaseAuth.currentUser
        val currentUser = currentUser_!!.uid
        val database = FirebaseDatabase.getInstance()
        var curr_point:Int = 0

        val usersRef = Firebase.firestore
        usersRef.collection("User").document(currentUser).get()
            .addOnSuccessListener { document ->
                var user_ = user()
                if (document != null)
                {
                    user_ = document.toObject(user::class.java)!!
                }
                userName.text = user_.UserName
                userFullName.setText(user_.Name)
                userSex.setText(user_.Sex)
                userPhoneNumber.setText(user_.PhoneNumber)
                userEmail.setText(user_.Email)
                userBD.setText(user_.Birthday)
                userAddress.setText(user_.Address)
                curr_point = user_.Point
            }
            .addOnFailureListener{exception ->
                Log.e("TAG", "User data not found")
            }


        val LogOutBtn = findViewById<Button>(R.id.LogOutBtn)
        LogOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@UserDetailFragment, LoginFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        val BackUser = findViewById<ImageView>(R.id.iVBack)
        BackUser.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        val myIntent = intent
        val pwStr = myIntent.getStringExtra("Pw").toString()
        tVChangePw.setOnClickListener {
            val intent = Intent(this@UserDetailFragment, ChangePasswordActivity::class.java)
            intent.putExtra("Pw", pwStr)
            startActivity(intent)
        }
        val ChangeBtn = findViewById<TextView>(R.id.tVChangePersonalData)
        val SaveBtn = findViewById<TextView>(R.id.SaveBtn)
        ChangeBtn.setOnClickListener {
            userFullName.isEnabled = true
            userSex.isEnabled = true
            userPhoneNumber.isEnabled = true
//            userEmail.isEnabled = true
            userBD.isEnabled = true
            userAddress.isEnabled = true
            SaveBtn.isVisible = true
        }
        SaveBtn.setOnClickListener {
            val user_ = user(
                currentUser,
                userEmail.text.toString(),
                userPhoneNumber.text.toString(),
                userName.text.toString(),
                userAddress.text.toString(),
                userSex.text.toString(),
                userFullName.text.toString(),
                userBD.text.toString(),
                curr_point
            )
            usersRef.collection("User").document(currentUser).set(user_).addOnSuccessListener {
                Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Lưu thông tin thất bại", Toast.LENGTH_SHORT).show()
            }
            val newEmail = userEmail.text.toString()
        }
    }
}