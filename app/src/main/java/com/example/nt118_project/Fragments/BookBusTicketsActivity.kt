package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BookBusTicketsActivity : AppCompatActivity() {
    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var DepartureDaytV:TextView
    private lateinit var ReturnDaytV:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_bus_tickets)

        val ReturnBtn = findViewById<ImageView>(R.id.iVBack)

        ReturnBtn.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        DepartureDaytV = findViewById<TextView>(R.id.DepartureDaySpinner)
        ReturnDaytV = findViewById<TextView>(R.id.ReturnDaySpinner)

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
    }

    private fun updateLable(c: Calendar, tV:TextView) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText(sdf.format(c.time))
    }
}