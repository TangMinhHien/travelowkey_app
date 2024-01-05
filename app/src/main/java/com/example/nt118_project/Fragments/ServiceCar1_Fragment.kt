package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ServiceCar1_Fragment: Fragment() {

    private lateinit var DeparturePlace: Spinner
    private lateinit var Duration: Spinner
    private lateinit var TimeDeparture: TextView
    private lateinit var DepartureDay: TextView
    private lateinit var EndDay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_servicecar_1, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DeparturePlace = view.findViewById<Spinner>(R.id.sp_destination_place)
        Duration = view.findViewById<Spinner>(R.id.sp_duration)
        TimeDeparture = view.findViewById<TextView>(R.id.tv_timepick)
        DepartureDay = view.findViewById<TextView>(R.id.sp_date_start)
        EndDay = view.findViewById<TextView>(R.id.tv_date_end)
        val c: Calendar = Calendar.getInstance()

        // Datepicker for Depature time
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DepartureDay)
        }

        DepartureDay.setOnClickListener {
            DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        val currentDate = LocalDate.now()
        val currentDay = currentDate.dayOfMonth
        val currentMonth = currentDate.monthValue
        val currentYear = currentDate.year
        DepartureDay.text = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
        EndDay.text = "Thời gian kết thúc: "+ currentDay.toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()

        // EditText Time
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            updateText(c, TimeDeparture)
        }

        TimeDeparture.setOnClickListener {
            TimePickerDialog(view.context, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
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

        // Data of Duration
        val DurationSpinnerData: ArrayList<Any?> = ArrayList()
        DurationSpinnerData.add("1")
        DurationSpinnerData.add("2")
        DurationSpinnerData.add("3")
        DurationSpinnerData.add("4")
        DurationSpinnerData.add("5")
        DurationSpinnerData.add("6")
        DurationSpinnerData.add("7")
        DurationSpinnerData.add("8")
        DurationSpinnerData.add("9")
        DurationSpinnerData.add("10")
        DurationSpinnerData.add("11")
        DurationSpinnerData.add("12")
        DurationSpinnerData.add("13")
        DurationSpinnerData.add("14")
        val DurationAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DurationSpinnerData)
        DurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Duration.onItemSelectedListener = listener
        Duration.setAdapter(DurationAdapter)

        // Update End Date
        Duration?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sdf = DateTimeFormatter.ofPattern("d-M-yyyy")
                val dateDepart = LocalDate.parse(DepartureDay.text, sdf)
                val dateEnd = dateDepart.plusDays(Duration.selectedItem.toString().toLong()-1)
                EndDay.text = "Thời gian kết thúc: "+dateEnd.dayOfMonth.toString() + "-" + dateEnd.monthValue.toString() + "-" + dateEnd.year.toString()
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getValue(): Any {
        var value = Bundle()
        val sdf = DateTimeFormatter.ofPattern("d-M-yyyy")
        val dateDepart = LocalDate.parse(DepartureDay.text, sdf)
        val dateEnd = dateDepart.plusDays(Duration.selectedItem.toString().toLong()-1)
        value.putBoolean("return_check",false)
        value.putBoolean("is_return",false)
        value.putString("Date Depature",DepartureDay.text.toString())
        value.putString("Place",DeparturePlace.selectedItem.toString())
        value.putString("Duration",Duration.selectedItem.toString())
        value.putString("Time",TimeDeparture.text.toString())
        value.putString("Date End", dateEnd.toString())
        return value;
    }
}