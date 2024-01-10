package com.example.nt118_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nt118_project.Fragments.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class ForgetPwActivity : AppCompatActivity() {
    public lateinit var tVemail: EditText
    public lateinit var btnReset: Button
    public lateinit var btnBack: Button
    public lateinit var mAuth: FirebaseAuth
    public lateinit var strEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pw)

        tVemail = findViewById(R.id.email_signin)
        btnReset = findViewById(R.id.signin_button)
        btnBack = findViewById(R.id.signup_button)
        mAuth = FirebaseAuth.getInstance()

        btnReset.setOnClickListener {
            strEmail = tVemail.text.toString().trim()
            if(!TextUtils.isEmpty(strEmail))
            {
                ResetPw()
            }
            else
            {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_LONG).show()
            }
        }
        btnBack.setOnClickListener {
            val intent:Intent = Intent(this@ForgetPwActivity, LoginFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
    public fun ResetPw(){
        mAuth.sendPasswordResetEmail(strEmail)
            .addOnSuccessListener {
                Toast.makeText(this, "1", Toast.LENGTH_LONG).show()
                val intent:Intent = Intent(this@ForgetPwActivity, LoginFragment::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "2", Toast.LENGTH_LONG).show()
            }
    }
}