package com.example.nt118_project.Fragments

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
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

    fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
    private fun saveInfoUser(email: String, phone: String, username: String, fullname:String, progressDialog: ProgressDialog) {
        val currUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = Firebase.firestore
        val resend_btn: Button = findViewById(R.id.resend_button)
        data class user(
            val Id:String,
            val Email: String,
            val PhoneNumber: String,
            val UserName: String,
            val Address: String,
            val Sex: String,
            val Name: String,
            val Birthday: String,
            val Point: Int
        )

        val new_user = user(currUserID,email,phone,username,"Chưa được cập nhật","Chưa được cập nhật",fullname, "Chưa được cập nhật",0)
        val auth = FirebaseAuth.getInstance()
        auth.currentUser!!.sendEmailVerification().addOnSuccessListener {
            Toast.makeText(this, "Email xác nhận đã được gửi.", Toast.LENGTH_SHORT).show()
//            if (auth.currentUser!!.isEmailVerified() == true)
//            {
//                usersRef.collection("User").document(currUserID).set(new_user).addOnCompleteListener {task ->
//                    if(task.isSuccessful)
//                    {
//                        progressDialog.dismiss()
//                        Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this@SignupFragment, MainActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                        startActivity(intent)
//                        finish()
//                    }
//                    else{
//                        val mess = task.exception!!.toString()
//                        Toast.makeText(this, "Lỗi: $mess", Toast.LENGTH_LONG).show()
//                        FirebaseAuth.getInstance().signOut()
//                        progressDialog.dismiss()
//                    }
//                }
//            }
//            else
//            {
//                resend_btn.setVisibility(View.VISIBLE)
//            }
        }
        resend_btn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser!!.sendEmailVerification().addOnSuccessListener {
                Toast.makeText(this, "Email xác nhận đã được gửi lại.", Toast.LENGTH_SHORT).show()
            }
        }
//        if (auth.currentUser!!.isEmailVerified() == true)
//        {
            usersRef.collection("User").document(currUserID).set(new_user).addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    val ListIdCoupon = ArrayList<String>()
                    ListIdCoupon.add("")
                    val newUsedCouponId = "UsedCoupon"+generateRandomString(14)
                    val new_usedcoupon: UsedCoupon = UsedCoupon(ListIdCoupon,newUsedCouponId, currUserID)
                    usersRef.collection("UsedCoupon").document(newUsedCouponId).set(new_usedcoupon).addOnCompleteListener{task ->
                        if (task.isSuccessful)
                        {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignupFragment, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            val pw_  = findViewById<EditText>(R.id.pw_signup)
                            val pw = pw_.text.toString()
                            intent.putExtra("Pw", pw)
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
                else{
                    val mess = task.exception!!.toString()
                    Toast.makeText(this, "Lỗi: $mess", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
       // }
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