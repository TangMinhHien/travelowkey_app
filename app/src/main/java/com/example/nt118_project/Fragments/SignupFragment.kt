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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.DatabaseMetaData

class SignupFragment : AppCompatActivity() {
    private fun createAccount() {
        val email_  = findViewById<EditText>(R.id.email_signup)
        val phone_  = findViewById<EditText>(R.id.phone_number_signup)
        val username_  = findViewById<EditText>(R.id.username_signup)
        val pw_  = findViewById<EditText>(R.id.pw_signup)

        val email = email_.text.toString()
        val phone = phone_.text.toString()
        val username = username_.text.toString()
        val pw = pw_.text.toString()

        when{
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(phone) -> Toast.makeText(this, "Vui lòng nhập SĐT", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(username) -> Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show()
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
                        saveInfoUser(email, phone, username, pw, progressDialog)

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

    private fun saveInfoUser(email: String, phone: String, username: String, pw: String, progressDialog: ProgressDialog) {
        val currUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("KhachHang")

        val usermap = HashMap<String, Any>()
        usermap["id"] = currUserID
        usermap["email"] = email
        usermap["phone_number"] = phone
        usermap["username"] = username
        usermap["pw"] = pw
        usermap["address"] = "Chưa được cập nhật"
        usermap["sex"] = "Chưa được cập nhật"
        usermap["Name"] = "Chưa được cập nhật"

        usersRef.child(currUserID).setValue(usermap).addOnCompleteListener { task ->
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