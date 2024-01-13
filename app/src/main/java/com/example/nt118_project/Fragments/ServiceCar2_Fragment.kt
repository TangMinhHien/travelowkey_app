package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.Duration
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
    private lateinit var db: FirebaseFirestore
    private lateinit var ref_place: CollectionReference

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
            val DatePickerDialog= DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }

        EndDay.setOnClickListener {
            val DatePickerDialog= DatePickerDialog(view.context, datepicker_end, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }

        val  formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val Date = currentDate
        DepartureDay.text = Date

        val sdf_ = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dateDepart = LocalDate.parse(Date, sdf_)
        val dateEnd = dateDepart.plusDays(1)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateEnd.toString())
        val EndDate= outputFormat.format(date!!)
        EndDay.text = EndDate

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


//        val currentTime = LocalTime.now ()
//        val currentHour = currentTime.hour
//        val currentMinute = currentTime.minute
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 12 hrs format (ranging from 0-11)
        val currentMinute: Int = rightNow.get(Calendar.MINUTE)
        val time = LocalTime.parse(currentHour.toString() + ":" + currentMinute.toString(), DateTimeFormatter.ofPattern("H:m"))
        DepartureTime.text = time.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        EndTime.text = time.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        Log.d("DepartureTime", DepartureTime.text.toString())

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

        // Connect Database
        db = Firebase.firestore
        ref_place = db.collection("ServiceCar_PickingPoint")
        val DeparturePlaceSpinnerData: ArrayList<Any?> = ArrayList()
        ref_place.get().addOnSuccessListener { documents ->
            for (document in documents){
                DeparturePlaceSpinnerData.add(document.get("area"))
            }
            val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item, DeparturePlaceSpinnerData)
            StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            DeparturePlace.onItemSelectedListener = listener
            DeparturePlace.setAdapter(StartingPointAdapter)
        }

//        // Data of Depature Place
//        val DeparturePlaceSpinnerData: ArrayList<Any?> = ArrayList()
//        DeparturePlaceSpinnerData.add("Sân bay Tân Sơn Nhất")
//        DeparturePlaceSpinnerData.add("Sân bay Nội Bài")
//        DeparturePlaceSpinnerData.add("Sân bay Cát Bi")
//        DeparturePlaceSpinnerData.add("Sân bay Cam Ranh")
//        DeparturePlaceSpinnerData.add("Sân bay Phú Bài")
//        val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DeparturePlaceSpinnerData)
//        StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        DeparturePlace.onItemSelectedListener = listener
//        DeparturePlace.setAdapter(StartingPointAdapter)
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

    fun getValue(): Bundle {
        var value = Bundle()
        value.putBoolean("return_check",true)
        value.putBoolean("is_return",false)
        value.putString("Place", DeparturePlace.selectedItem.toString())
        value.putString("DateDepature", DepartureDay.text.toString())
        value.putString("TimeDepature", DepartureTime.text.toString())
        value.putString("DateEnd", EndDay.text.toString())
        value.putString("TimeEnd", EndTime.text.toString())
        return value;
    }
}