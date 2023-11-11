package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BookBusTicketsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var DepartureDaytV:TextView
    private lateinit var ReturnDaytV:TextView
    private lateinit var SpinnerSeat: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_bus_tickets)

        val ReturnBtn = findViewById<ImageView>(R.id.iVBack)
        DepartureDaytV = findViewById<TextView>(R.id.DepartureDaySpinner)
        ReturnDaytV = findViewById<TextView>(R.id.ReturnDaySpinner)
        SpinnerSeat = findViewById<Spinner>(R.id.SeatSpinner)

        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }

        val c: Calendar = Calendar.getInstance()
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DepartureDaytV)
        }
        val datepicker_return = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, ReturnDaytV)
        }
        DepartureDaytV.setOnClickListener {
            DatePickerDialog(this, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        ReturnDaytV.setOnClickListener {
            DatePickerDialog(this, datepicker_return, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        val SeatSpinnerData: ArrayList<Any?> = ArrayList()
        SeatSpinnerData.add("1 ghế ngồi")
        SeatSpinnerData.add("2 ghế ngồi")
        SeatSpinnerData.add("3 ghế ngồi")
        SeatSpinnerData.add("4 ghế ngồi")
        val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,SeatSpinnerData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        val listener: AdapterView.OnItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View,
//                    position: Int,
//                    id: Long
//                ) {
//                    (parent.getChildAt(0) as TextView).setTextColor(0x00000000)
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//            }
//        SpinnerSeat.setOnItemSelectedListener(listener);
        SpinnerSeat.onItemSelectedListener = this
        SpinnerSeat.setAdapter(adapter)
    }

    private fun updateLable(c: Calendar, tV:TextView) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText(sdf.format(c.time))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Selected","Select Seat")
        (view as TextView).setTextColor(Color.BLACK)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("Selected","Not select seat")
    }
}