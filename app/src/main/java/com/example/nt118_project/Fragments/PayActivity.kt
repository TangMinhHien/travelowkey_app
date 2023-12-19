package com.example.nt118_project.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nt118_project.R

class PayActivity : AppCompatActivity() {
    private lateinit var FillUserRadio: RadioButton
    private lateinit var ReviewUserRadio: RadioButton
    private lateinit var PayUserRadio: RadioButton
    private lateinit var LayoutContainer: FrameLayout
    private lateinit var RadioGroup: RadioGroup
    private lateinit var ReturnBtn:ImageView
    private lateinit var NextBtn: Button
    private var FirstID:String = ""
    private var SecondID: String = ""
    private var NumberOfSeat: String = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        val myIntent = intent // this is just for example purpose
        val isRoundTrip: Boolean = myIntent.getStringExtra("RoundTrip").toBoolean()
        FirstID = myIntent.getStringExtra("FirstSelectedID").toString()
        SecondID = myIntent.getStringExtra("SecondSelectedID").toString()
        NumberOfSeat = myIntent.getStringExtra("Seat").toString()

        FillUserRadio = findViewById<RadioButton>(R.id.radio_button1)
        ReviewUserRadio = findViewById<RadioButton>(R.id.radio_button2)
        PayUserRadio = findViewById<RadioButton>(R.id.radio_button3)
        LayoutContainer = findViewById<FrameLayout>(R.id.frame_layout)
        RadioGroup = findViewById<RadioGroup>(R.id.radio_group)
        ReturnBtn = findViewById<ImageView>(R.id.iVBack)
        NextBtn = findViewById<Button>(R.id.Savebutton)

        FillUserRadio.isEnabled = true

        RadioGroup.setOnCheckedChangeListener {group, checkedId ->
            when(checkedId){
                R.id.radio_button2 -> {
                        ReviewUserRadio.isEnabled = true
                        val fragobj = ReviewUserInfoFragmentFragment()
                        val bundle = Bundle()
                        bundle.putString("FirstID", FirstID)
                        bundle.putString("SecondID", SecondID)
                        fragobj.setArguments(bundle)
                        replaceFragment(fragobj)
                        NextBtn.text = "Tiếp tục"
                }
                R.id.radio_button3 -> {
                    PayUserRadio.isEnabled = true
                    val fragobj = PaymentInfoFragment()
                    val bundle = Bundle()
                    bundle.putString("FirstID", FirstID)
                    bundle.putString("SecondID", SecondID)
                    bundle.putString("Seat", NumberOfSeat)
                    fragobj.setArguments(bundle)
                    replaceFragment(fragobj)
                    NextBtn.text = "Hoàn tất"
                }
                R.id.radio_button1 -> {
                        val fragobj = FillUserInfoFragment()
                        val bundle = Bundle()
                        bundle.putString("FirstID", FirstID)
                        bundle.putString("SecondID", SecondID)
                        fragobj.setArguments(bundle)
                        replaceFragment(fragobj)
                        NextBtn.text = "Tiếp tục"
                }
            }
        }

        FillUserRadio.performClick()

        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        NextBtn.setOnClickListener {
            val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_layout)

            if (currentFragment is FillUserInfoFragment) {
                ReviewUserRadio.performClick()
            } else if (currentFragment is ReviewUserInfoFragmentFragment) {
                PayUserRadio.performClick()
            }
        }

    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}