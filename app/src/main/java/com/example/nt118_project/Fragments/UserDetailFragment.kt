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
import com.example.nt118_project.Model.User
import com.example.nt118_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser_: FirebaseUser? = firebaseAuth.currentUser
        val currentUser = currentUser_!!.uid
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("KhachHang").child(currentUser)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    userName.text = user.username
                    userFullName.setText(user.Name)
                    userSex.setText(user.sex)
                    userPhoneNumber.setText(user.phone_number)
                    userEmail.setText(user.email)
                    userBD.setText(user.birthday)
                    userAddress.setText(user.address)

                } else {
                    Log.e("TAG", "User data not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException())
            }
        })

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
        val ChangeBtn = findViewById<TextView>(R.id.tVChangePersonalData)
        val SaveBtn = findViewById<Button>(R.id.SaveBtn)
        ChangeBtn.setOnClickListener {
            userFullName.isEnabled = true
            userSex.isEnabled = true
            userPhoneNumber.isEnabled = true
            userEmail.isEnabled = true
            userBD.isEnabled = true
            userAddress.isEnabled = true
            SaveBtn.isVisible = true
        }
        SaveBtn.setOnClickListener {
            val user_ = User(
                userFullName.text.toString(),
                userName.text.toString(),
                userAddress.text.toString(),
                userEmail.text.toString(),
                userPhoneNumber.text.toString(),
                userSex.text.toString(),
                currentUser,
                userBD.text.toString()
            )
            userRef.setValue(user_).addOnSuccessListener {
                Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Lưu thông tin thất bại", Toast.LENGTH_SHORT).show()
            }
            val newEmail = userEmail.text.toString()
//            currentUser_!!.updateEmail(userEmail.text.toString()).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Email đăng nhập của bạn đã được cập nhật thành: $newEmail", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Không cập nhật được email: ${task.exception}", Toast.LENGTH_SHORT).show()
//                }
//            }
        }
    }
}