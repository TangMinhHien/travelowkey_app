package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nt118_project.MainActivity
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import java.sql.DatabaseMetaData

class SignupFragment : AppCompatActivity() {
    private fun createAccount() {
        val email_  = findViewById<EditText>(R.id.email_signup)
        val phone_  = findViewById<EditText>(R.id.phone_number_signup)
        val username_  = findViewById<EditText>(R.id.username_signup)
        val pw_  = findViewById<EditText>(R.id.pw_signup)
        val fullname_ = findViewById<EditText>(R.id.fullname_signup)

        val email = email_.text.toString()
        val phone = phone_.text.toString()
        val username = username_.text.toString()
        val pw = pw_.text.toString()
        val fullname = fullname_.text.toString()

        when{
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(phone) -> Toast.makeText(this, "Vui lòng nhập SĐT", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(username) -> Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(fullname) -> Toast.makeText(this, "Vui lòng nhập họ tên đầy đủ", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(pw) -> Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this@SignupFragment)
                progressDialog.setTitle("Đăng ký")
                progressDialog.setMessage("Vui lòng đợi...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        saveInfoUser(email, phone, username,fullname, progressDialog)

                    }
                    else
                    {
                        val mess = task.exception!!.toString()
                        Toast.makeText(this, "Lỗi: $mess", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }

    }

    private fun saveInfoUser(email: String, phone: String, username: String, fullname:String, progressDialog: ProgressDialog) {
        val currUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = Firebase.firestore

        data class user(
            val Id:String,
            val Email: String,
            val PhoneNumber: String,
            val UserName: String,
            val Address: String,
            val Sex: String,
            val Name: String,
            val Birthday: String
        )

        val new_user = user(currUserID,email,phone,username,"Chưa được cập nhật","Chưa được cập nhật",fullname, "Chưa được cập nhật")

        usersRef.collection("User").document(currUserID).set(new_user).addOnCompleteListener {task ->
            if(task.isSuccessful)
            {
                progressDialog.dismiss()
                Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignupFragment, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else{
                val mess = task.exception!!.toString()
                Toast.makeText(this, "Lỗi: $mess", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_fragment)

        val signin_button = findViewById<Button>(R.id.signin_button)

        signin_button.setOnClickListener {
            startActivity(Intent(this, LoginFragment::class.java))
        }

        val signup_button = findViewById<Button>(R.id.signup_button)

        signup_button.setOnClickListener {
            createAccount()
        }
    }
}