package com.example.nt118_project.Fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.nt118_project.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val iVReturn = findViewById<ImageView>(R.id.iVBack)
        iVReturn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        val SaveBtn = findViewById<Button>(R.id.Savebutton)
        val tVEmail = findViewById<EditText>(R.id.eTEmail)
        val tVOlePw = findViewById<EditText>(R.id.eTOldPw)
        val tVNewPw_1:EditText = findViewById<EditText>(R.id.eTNewPw_1)
        val tVNewPw_2 = findViewById<EditText>(R.id.eTNewPw_2)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currUser: FirebaseUser? = firebaseAuth.currentUser
        val receivedPw = intent.getStringExtra("Pw").toString()
        val newPw:String  =tVNewPw_1.text.toString()
        SaveBtn.setOnClickListener {
            if (tVEmail.text.toString() == "" || tVOlePw.text.toString() == "" || tVNewPw_1.text.toString() == null || tVNewPw_2.text.toString() == "")
            {
                Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show()
            }
            else if(currUser!!.email.toString() != tVEmail.text.toString())
            {
                Toast.makeText(this,"Bạn nhập sai Email", Toast.LENGTH_LONG).show()
            }
            else if(tVNewPw_1.text.toString() != tVNewPw_2.text.toString())
            {
                Toast.makeText(this,"Bạn xác minh lại không đúng password", Toast.LENGTH_LONG).show()
            }
            else if(tVOlePw.text.toString() != receivedPw)
            {
                Toast.makeText(this,"Bạn nhập sai password cũ", Toast.LENGTH_LONG).show()
            }
            else {
                val firebaseAuth = FirebaseAuth.getInstance()
                val curr: FirebaseUser? = firebaseAuth.currentUser
                if (curr != null && curr.email != null) {
                    val credential = EmailAuthProvider.getCredential(
                        tVEmail.text.toString(),
                        tVOlePw.text.toString()
                    )
                    curr.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            curr?.updatePassword(tVNewPw_1.text.toString())?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Thay đổi thành công. Mật khẩu mới: " + tVNewPw_1.text.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    //firebaseAuth.signOut()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Thay đổi mật khẩu thất bại",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    //firebaseAuth.signOut()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}