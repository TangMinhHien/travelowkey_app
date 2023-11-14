package com.example.nt118_project.hotel

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SearchHotelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_hotel)
        val listHotelBtn = findViewById<Button>(R.id.btn_search)
        listHotelBtn.setOnClickListener{
            val intent = Intent(this, ListofHotelsActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val backBtn = findViewById<ImageButton>(R.id.search_hotel_back)
        backBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        val DaytV = findViewById<TextView>(R.id.sp_date)
        val c: Calendar = Calendar.getInstance()
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DaytV)
        }
        DaytV.setOnClickListener {
            DatePickerDialog(this, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val Address = findViewById<Spinner>(R.id.sp_address)
        val AddressData: ArrayList<Any?> = ArrayList()
        AddressData.add("TP. HCM")
        AddressData.add("TP. Đà Nẵng")
        AddressData.add("TP. Hà Nội")
        AddressData.add("TP. Bà Rịa - Vũng Tàu")
        val AddressAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this,android.R.layout.simple_spinner_item,AddressData)
        AddressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Address.onItemSelectedListener = listener
        Address.setAdapter(AddressAdapter)

        val SpinnerNumber = findViewById<Spinner>(R.id.sp_room)
        val NumberSpinnerData: ArrayList<Any?> = ArrayList()
        NumberSpinnerData.add("1 người")
        NumberSpinnerData.add("2 người")
        NumberSpinnerData.add("3 người")
        NumberSpinnerData.add("4 người")
        val NumberAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this,android.R.layout.simple_spinner_item,NumberSpinnerData)
        NumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerNumber.onItemSelectedListener = listener
        SpinnerNumber.setAdapter(NumberAdapter)
    }
    private fun updateLable(c: Calendar, tV: TextView) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText(sdf.format(c.time))
    }
}