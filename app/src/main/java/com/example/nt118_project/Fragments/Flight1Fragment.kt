package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Flight1Fragment : Fragment() {

    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var DepartureDaytV: TextView
    private lateinit var SpinnerStartingPoint: Spinner
    private lateinit var SpinnerDestination: Spinner
    private lateinit var SpinnerNumber: Spinner
    private lateinit var SpinnerSeat: Spinner
    private lateinit var db: FirebaseFirestore
    private lateinit var ref_from: CollectionReference
    private lateinit var ref_to: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight1, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DepartureDaytV = view.findViewById<TextView>(R.id.sp_date)
        SpinnerStartingPoint = view.findViewById<Spinner>(R.id.sp_from)
        SpinnerDestination = view.findViewById<Spinner>(R.id.sp_to)
        SpinnerNumber = view.findViewById<Spinner>(R.id.sp_number)
        SpinnerSeat = view.findViewById<Spinner>(R.id.sp_chair)
        val c: Calendar = Calendar.getInstance()
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DepartureDaytV)
        }
        DepartureDaytV.setOnClickListener {
            val DatePickerDialog= DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }
        val currentDate = LocalDate.now()
        val currentDay = currentDate.dayOfMonth
        val currentMonth = currentDate.monthValue
        val currentYear = currentDate.year
        DepartureDaytV.text = currentDay.toString()+"-"+currentMonth.toString()+"-"+currentYear.toString()
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        db = Firebase.firestore
        ref_from = db.collection("Flight_starting_point")
        val StartingPointSpinnerData: ArrayList<Any?> = ArrayList()
        ref_from.get().addOnSuccessListener { documents ->
            for (document in documents){
                StartingPointSpinnerData.add(document.get("Area"))
            }
            val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,StartingPointSpinnerData)
            StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            SpinnerStartingPoint.onItemSelectedListener = listener
            SpinnerStartingPoint.setAdapter(StartingPointAdapter)
        }


        ref_to = db.collection("Flight_destination")
        val DestinationSpinnerData: ArrayList<Any?> = ArrayList()
        ref_to.get().addOnSuccessListener { documents ->
            for (document in documents){
                DestinationSpinnerData.add(document.get("Area"))
            }
            val DestinationAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DestinationSpinnerData)
            DestinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            SpinnerDestination.onItemSelectedListener = listener
            SpinnerDestination.setAdapter(DestinationAdapter)        }


        val NumberSpinnerData: ArrayList<Any?> = ArrayList()
        NumberSpinnerData.add("1")
        NumberSpinnerData.add("2")
        NumberSpinnerData.add("3")
        NumberSpinnerData.add("4")
        val NumberAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,NumberSpinnerData)
        NumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerNumber.onItemSelectedListener = listener
        SpinnerNumber.setAdapter(NumberAdapter)

        val SeatSpinnerData: ArrayList<Any?> = ArrayList()
        SeatSpinnerData.add("Phổ thông")
        SeatSpinnerData.add("Phổ thông đặc biệt")
        SeatSpinnerData.add("Thương gia")
        val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context, android.R.layout.simple_spinner_item,SeatSpinnerData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerSeat.onItemSelectedListener = listener
        SpinnerSeat.setAdapter(adapter)
    }
    private fun updateLable(c: Calendar, tV: TextView) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText(sdf.format(c.time))
    }

    fun getValue():Bundle{
        var value = Bundle()
        value.putBoolean("return_check",false)
        value.putBoolean("is_return",false)
        value.putString("Date",DepartureDaytV.text.toString())
        value.putString("From",SpinnerStartingPoint.selectedItem.toString())
        value.putString("To",SpinnerDestination.selectedItem.toString())
        value.putString("NumSeat",SpinnerNumber.selectedItem.toString())
        value.putString("SeatClass",SpinnerSeat.selectedItem.toString())
        return value;
    }
}