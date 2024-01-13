package com.example.nt118_project.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class ServiceCar1_Fragment: Fragment() {

    private lateinit var DeparturePlace: Spinner
    private lateinit var Duration: Spinner
    private lateinit var TimeDeparture: TextView
    private lateinit var DepartureDay: TextView
    private lateinit var EndDay: TextView
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
            val DatePickerDialog= DatePickerDialog(view.context, datepicker_depart, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(
                Calendar.DAY_OF_MONTH))
            DatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            DatePickerDialog.show()
        }

        val  formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = LocalDate.now().format(formatter)
        val Date = currentDate
        DepartureDay.text = Date
        //EndDay.text = "Thời gian kết thúc: "+ currentDay.toString() + "-" + currentMonth.toString() + "-" + currentYear.toString()

        // EditText Time
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            updateText(c, TimeDeparture)
        }

        TimeDeparture.setOnClickListener {
            TimePickerDialog(view.context, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR) // return the hour in 12 hrs format (ranging from 0-11)
        val currentMinute: Int = rightNow.get(Calendar.MINUTE)

        val time = LocalTime.parse(currentHour.toString() + ":" + currentMinute.toString(), DateTimeFormatter.ofPattern("H:m"))
        TimeDeparture.text  = time.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        Log.d("DepartureTime", TimeDeparture.text.toString())

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
////        val DeparturePlaceSpinnerData: ArrayList<Any?> = ArrayList()
////        DeparturePlaceSpinnerData.add("Sân bay Tân Sơn Nhất")
////        DeparturePlaceSpinnerData.add("Sân bay Nội Bài")
////        DeparturePlaceSpinnerData.add("Sân bay Cát Bi")
////        DeparturePlaceSpinnerData.add("Sân bay Cam Ranh")
////        DeparturePlaceSpinnerData.add("Sân bay Phú Bài")
//        val StartingPointAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(view.context,android.R.layout.simple_spinner_item,DeparturePlaceSpinnerData)
//        StartingPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        DeparturePlace.onItemSelectedListener = listener
//        DeparturePlace.setAdapter(StartingPointAdapter)

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
        Duration.setAdapter(DurationAdapter)

        // Update End Date
        Duration?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Duration.setSelection(0)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.WHITE)
                val sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val dateDepart = LocalDate.parse(DepartureDay.text, sdf)
                val dateEnd = dateDepart.plusDays(Duration.selectedItem.toString().toLong()-1)

                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateEnd.toString())
                val Date= outputFormat.format(date!!)
                EndDay.text = Date
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLable(c: Calendar, tV: TextView) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tV.setText (sdf.format(c.time))

        val sdf_ = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dateDepart = LocalDate.parse(tV.text, sdf_)
        val dateEnd = dateDepart.plusDays(Duration.selectedItem.toString().toLong()-1)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateEnd.toString())
        val Date= outputFormat.format(date!!)
        EndDay.text = Date
    }

    private fun updateText (c: Calendar, et: TextView) {
        val myFormat = "hh:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        et.setText(sdf.format(c.time))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getValue(): Bundle {
        var value = Bundle()
//        val sdf = DateTimeFormatter.ofPattern("d-M-yyyy")
//        val dateDepart = LocalDate.parse(DepartureDay.text, sdf)
//        var dateEnd = dateDepart.plusDays(Duration.selectedItem.toString().toLong()-1)
//        dateEnd = LocalDate.parse(dateEnd.toString(), sdf)
        value.putBoolean("return_check",false)
        value.putBoolean("is_return",false)
        value.putString("DateDepature",DepartureDay.text.toString())
        value.putString("Place",DeparturePlace.selectedItem.toString())
        value.putString("Duration",Duration.selectedItem.toString())
        value.putString("Time",TimeDeparture.text.toString())
        value.putString("DateEnd", EndDay.text.toString())
        return value;
    }
}