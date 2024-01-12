package com.example.nt118_project.Fragments

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class debitCard{
    public var cvvnumber:String = ""
    public var expiryDate: String = ""
    public var id: String = ""
    public var nameCard: String = ""
    public var numberCard: String = ""
    public var user_Id: String = ""

    constructor()
    constructor(CVVNumber:String, expiryDate:String, id:String, nameCard: String, numberCard:String, user_Id:String)
    {
        this.cvvnumber = CVVNumber
        this.expiryDate = expiryDate
        this.id = id
        this.nameCard = nameCard
        this.numberCard = numberCard
        this.user_Id = user_Id
    }
}
class DebitCardActivity : AppCompatActivity() {
    fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debit_card)

        val eTCardNumber:EditText = findViewById(R.id.eTCardNumber)
        val eTExpiryDay:EditText = findViewById(R.id.eTDay)
        val eTExpiryYear:EditText = findViewById(R.id.eTYear)
        val eTCVVNUmber:EditText = findViewById(R.id.eTCVV)
        val eTNameOnCard:EditText = findViewById(R.id.eTName)

        val tVNoteCardNumber:TextView = findViewById(R.id.tVNoteCardNumber)
        val tVNoteExpiryDate:TextView = findViewById(R.id.tVNoteDate)
        val tVNoteCVVNumber:TextView = findViewById(R.id.tVNoteCVV)
        val tVNoteNameOnCard:TextView = findViewById(R.id.tVNoteName)
        val ic_info:ImageView = findViewById(R.id.ic_info)

        ic_info.setOnClickListener {
            val builder = Dialog(this@DebitCardActivity)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
            builder.setTitle("Chi tiết hóa đơn")
            val view: View =
                LayoutInflater.from(this@DebitCardActivity).inflate(R.layout.debit_card_cvv_info_dialog, null)
            builder.setContentView(view)
            builder.show()
        }

        val btnSave: Button = findViewById(R.id.SaveBtn)

        val BackUser = findViewById<ImageView>(R.id.iVBack)
        BackUser.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser_: FirebaseUser? = firebaseAuth.currentUser
        val currentUser = currentUser_!!.uid

        val usersRef = Firebase.firestore
        val formatter = DateTimeFormatter.ofPattern("MM/yy")
        val currentDay = LocalDateTime.now().format(formatter)
        val sdf = SimpleDateFormat("MM/yy")
        val strcurrentDay: Date = sdf.parse(currentDay)
        usersRef.collection("DebitCard").whereEqualTo("user_Id", currentUser).get()
            .addOnSuccessListener { documents ->
                var card_ = debitCard()
                if(documents.size() != 0)
                {
                    for (document in documents)
                    {
                        card_ = document.toObject(debitCard::class.java)!!
                        if (document != null)
                        {
                            btnSave.text = "Lưu thông tin"
                            eTCardNumber.setText(card_.numberCard)
                            eTExpiryDay.setText(card_.expiryDate.substring(0,2))
                            eTExpiryYear.setText(card_.expiryDate.substring(card_.expiryDate.length-2, card_.expiryDate.length))
                            eTCVVNUmber.setText(card_.cvvnumber)
                            eTNameOnCard.setText(card_.nameCard)

                            tVNoteCardNumber.setVisibility(View.GONE)
                            tVNoteExpiryDate.setVisibility(View.GONE)
                            tVNoteCVVNumber.setVisibility(View.GONE)
                            tVNoteNameOnCard.setVisibility(View.GONE)

                            btnSave.setOnClickListener {
                                if(eTCardNumber.text.length < 12)
                                    tVNoteCardNumber.visibility = View.VISIBLE
                                else tVNoteCardNumber.setVisibility(View.GONE)
                                if(eTExpiryDay.text.toString() == "" || eTExpiryYear.text.toString() == "") {
                                    tVNoteExpiryDate.text = "Mục bắt buộc"
                                    tVNoteExpiryDate.visibility = View.VISIBLE
                                }
                                else if(eTExpiryDay.text.toString().toInt() < 1 || eTExpiryDay.text.toString().toInt() > 12)
                                {
                                    tVNoteExpiryDate.text = "Tháng có giá trị từ 01-12"
                                    tVNoteExpiryDate.visibility = View.VISIBLE
                                }
                                else if(eTExpiryDay.text.length != 2 || eTExpiryYear.text.length != 2)
                                {
                                    tVNoteExpiryDate.text = "Ngày và năm cần theo đúng form"
                                    tVNoteExpiryDate.visibility = View.VISIBLE
                                }
                                else if(sdf.parse(eTExpiryDay.text.toString()+"/"+eTExpiryYear.text.toString()).before(strcurrentDay))
                                {
                                    tVNoteExpiryDate.text = "Thẻ đã hết hạn"
                                    tVNoteExpiryDate.visibility = View.VISIBLE
                                }
                                else tVNoteExpiryDate.setVisibility(View.GONE)
                                if(eTCVVNUmber.text.length < 3)
                                    tVNoteCVVNumber.visibility = View.VISIBLE
                                else tVNoteCVVNumber.setVisibility(View.GONE)
                                if(eTNameOnCard.text.toString() == "")
                                    tVNoteNameOnCard.visibility = View.VISIBLE
                                else tVNoteNameOnCard.setVisibility(View.GONE)
                                if(tVNoteCardNumber.visibility == View.GONE &&
                                    tVNoteExpiryDate.visibility == View.GONE &&
                                    tVNoteCVVNumber.visibility == View.GONE &&
                                    tVNoteNameOnCard.visibility == View.GONE)
                                {
                                    card_.numberCard = eTCardNumber.text.toString()
                                    card_.expiryDate = eTExpiryDay.text.toString()+"/"+eTExpiryYear.text.toString()
                                    card_.cvvnumber = eTCVVNUmber.text.toString()
                                    card_.nameCard = eTNameOnCard.text.toString()
                                    usersRef.collection("DebitCard").document(card_.id).set(card_).addOnSuccessListener {
                                        Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Lưu thông tin thất bại", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    btnSave.text = "Thêm thông tin thẻ"
                    tVNoteCardNumber.setVisibility(View.GONE)
                    tVNoteExpiryDate.setVisibility(View.GONE)
                    tVNoteCVVNumber.setVisibility(View.GONE)
                    tVNoteNameOnCard.setVisibility(View.GONE)

                    btnSave.setOnClickListener {
                        if(eTCardNumber.text.length < 12)
                            tVNoteCardNumber.setVisibility(View.VISIBLE)
                        else tVNoteCardNumber.setVisibility(View.GONE)
                        if(eTExpiryDay.text.toString() == "" || eTExpiryYear.text.toString() == "") {
                            tVNoteExpiryDate.text = "Mục bắt buộc"
                            tVNoteExpiryDate.visibility = View.VISIBLE
                        }
                        else if(eTExpiryDay.text.length != 2 || eTExpiryYear.text.length != 2)
                        {
                            tVNoteExpiryDate.text = "Tháng và năm cần theo đúng form"
                            tVNoteExpiryDate.visibility = View.VISIBLE
                        }
                        else if(eTExpiryDay.text.toString().toInt() < 1 || eTExpiryDay.text.toString().toInt() > 12)
                        {
                            tVNoteExpiryDate.text = "Tháng có giá trị từ 01-12"
                            tVNoteExpiryDate.visibility = View.VISIBLE
                        }
                        else if(sdf.parse(eTExpiryDay.text.toString()+"/"+eTExpiryYear.text.toString()).before(strcurrentDay))
                        {
                            tVNoteExpiryDate.text = "Thẻ đã hết hạn"
                            tVNoteExpiryDate.visibility = View.VISIBLE
                        }
                        else tVNoteExpiryDate.setVisibility(View.GONE)
                        if(eTCVVNUmber.text.length < 3)
                            tVNoteCVVNumber.setVisibility(View.VISIBLE)
                        else tVNoteCVVNumber.setVisibility(View.GONE)
                        if(eTNameOnCard.text.toString() == "")
                            tVNoteNameOnCard.setVisibility(View.VISIBLE)
                        else tVNoteNameOnCard.setVisibility(View.GONE)
                        if(tVNoteCardNumber.visibility == View.GONE &&
                            tVNoteExpiryDate.visibility == View.GONE &&
                            tVNoteCVVNumber.visibility == View.GONE &&
                            tVNoteNameOnCard.visibility == View.GONE)
                        {
                            val id: String = "DC" + generateRandomString(14)
                            val card_ = debitCard(eTCVVNUmber.text.toString(), eTExpiryDay.text.toString()+"/"+eTExpiryYear.text.toString(), id, eTNameOnCard.text.toString(), eTCardNumber.text.toString(), currentUser)
                            usersRef.collection("DebitCard").document(id).set(card_).addOnSuccessListener {
                                Toast.makeText(this, "Thêm thông tin thẻ thành công", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Thêm thông tin thẻ thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            .addOnFailureListener{exception ->
                Log.d("Exception", exception.toString())
            }
    }
}