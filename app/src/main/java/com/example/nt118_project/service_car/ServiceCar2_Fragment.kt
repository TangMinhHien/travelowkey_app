package com.example.nt118_project.service_car

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ServiceCar2_Fragment: Fragment() {
    private val context: Context? = null
    private lateinit var DeparturePlace: Spinner
    private lateinit var DepartureDay: TextView
    private lateinit var DepartureTime: TextView
    private lateinit var EndDay: TextView
    private lateinit var EndTime: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_servicecar_2, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DeparturePlace = view.findViewById<Spinner>(R.id.sp_destination_place)
        DepartureDay = view.findViewById<TextView>(R.id.tv_date_start)
        DepartureTime = view.findViewById<TextView>(R.id.tv_time_start)
        EndDay = view.findViewById<TextView>(R.id.tv_date_end)
        EndTime = view.findViewById<TextView>(R.id.tv_time_end)

        val c: Calendar = Calendar.getInstance()

        // Datepicker for Depature time
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DepartureDay)
        }

        val datepicker_end = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, EndDay)
        }

        DepartureDay.setOnClickListener {
            DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        EndDay.setOnClickListener {
            DatePickerDialog(view.context, datepicker_end, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        val currentDate = LocalDate.now()
        val currentDay = currentDate.dayOfMonth
        val currentMonth = currentDate.monthValue
        val currentYear = currentDate.year
        DepartureDay.text = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
        EndDay.text = currentDay.toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()

        val timeSetListener_DepatureTime = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            updateText(c, DepartureTime)
        }

        val timeSetListener_EndTime = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            updateText(c, EndTime)
        }

        DepartureTime.setOnClickListener {
            TimePickerDialog(view.context, timeSetListener_DepatureTime, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        val currentTime = LocalTime.now ()
        val currentHour = currentTime.hour
        val currentMinute = currentTime.minute
        DepartureTime.text = currentHour.toString() + ":" + currentMinute.toString()
        EndTime.text = currentHour.toString() + ":" + currentMinute.toString()

        var condition:Boolean = true
        EndTime.setOnClickListener {
            TimePickerDialog(view.context, timeSetListener_EndTime, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Data of Depature Place
        val DeparturePlaceSpinnerData: ArrayList<Any?> = ArrayList()
        DeparturePlaceSpinnerData.add("Sân bay Tân Sơn Nhất")
        DeparturePlaceSpinnerData.add("Sân bay Nội Bài")
        DeparturePlaceSpinnerData.add("Sân bay Cát Bi")
        DeparturePlaceSpinnerData.add("Sân bay Cam Ranh")
        DeparturePlaceSpinnerData.add("Sân bay Phú Bài")
        val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DeparturePlaceSpinnerData)
        StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        DeparturePlace.onItemSelectedListener = listener
        DeparturePlace.setAdapter(StartingPointAdapter)
    }

    private fun updateLable(c: Calendar, tV: TextView) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText (sdf.format(c.time))
    }

    private fun updateText (c: Calendar, et: TextView) {
        val myFormat = "hh:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        et.setText(sdf.format(c.time))
    }
}