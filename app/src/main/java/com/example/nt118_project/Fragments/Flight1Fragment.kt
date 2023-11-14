package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.graphics.Color
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
import com.example.nt118_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Flight1Fragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val DepartureDaytV = view.findViewById<TextView>(R.id.sp_date)
        val SpinnerStartingPoint = view.findViewById<Spinner>(R.id.sp_from)
        val SpinnerDestination = view.findViewById<Spinner>(R.id.sp_to)
        val SpinnerNumber = view.findViewById<Spinner>(R.id.sp_number)
        val SpinnerSeat = view.findViewById<Spinner>(R.id.sp_chair)
        val c: Calendar = Calendar.getInstance()
        val datepicker_depart = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(c, DepartureDaytV)
        }
        DepartureDaytV.setOnClickListener {
            DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val StartingPointSpinnerData: ArrayList<Any?> = ArrayList()
        StartingPointSpinnerData.add("TP. HCM")
        StartingPointSpinnerData.add("TP. Đà Nẵng")
        StartingPointSpinnerData.add("TP. Hà Nội")
        StartingPointSpinnerData.add("TP. Bà Rịa - Vũng Tàu")
        val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,StartingPointSpinnerData)
        StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerStartingPoint.onItemSelectedListener = listener
        SpinnerStartingPoint.setAdapter(StartingPointAdapter)

        val DestinationSpinnerData: ArrayList<Any?> = ArrayList()
        DestinationSpinnerData.add("TP. HCM")
        DestinationSpinnerData.add("TP. Đà Nẵng")
        DestinationSpinnerData.add("TP. Hà Nội")
        DestinationSpinnerData.add("TP. Bà Rịa - Vũng Tàu")
        val DestinationAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DestinationSpinnerData)
        DestinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerDestination.onItemSelectedListener = listener
        SpinnerDestination.setAdapter(DestinationAdapter)

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

}