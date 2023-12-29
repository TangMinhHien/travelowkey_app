package com.example.nt118_project.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Room
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
    fun formatter(n: Int) =
        DecimalFormat("#,###")
            .format(n)
            .replace(",", ".")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_payment_info, container, false)
        var TvTotal: TextView = rootView.findViewById<TextView>(R.id.tVTotal)
        val databaseReference = Firebase.firestore

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid

        val tag_ = this.arguments?.getString("Tag")

//        data class bus_invoice(
//            val Bus_Id_1:String,
//            val Bus_Id_2: String,
//            val Id: String,
//            val Invoice_Id: String
//        )
        data class service_invoice(
            val Id_ticket_1:String,
            val Id_ticket_2: String,
            val Id: String,
            val Invoice_Id: String
        )

        data class hotel_invoice(
            val CheckInDate:String,
            val CheckOutDate: String,
            val Id: String,
            val Invoice_Id:String,
            val RoomId: String
        )
        data class invoice(
            val Id: String,
            val Tag: String,
            val User_Id: String,
            val Num_Ticket: String,
            val Total: String
        )

        if(tag_ == "Bus" || tag_ == "Flight")
        {
            val FirstID = this.arguments?.getString("FirstID")
            val SecondID = this.arguments?.getString("SecondID")
            val NumberOfSeat = this.arguments?.getString("Seat")

            var Total:Double = 0.0
            var PayBtn: Button = rootView.findViewById<Button>(R.id.PayBtn)
            val dataModelList:ArrayList<Any> = ArrayList()
            if (NumberOfSeat != null) {
                databaseReference.collection(tag_).whereIn("Id", listOf(FirstID,SecondID))
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents)
                        {
                            if(tag_ == "Bus")
                            {
                                val dataModel= document.toObject(BusTicket::class.java)
                                dataModelList.add(dataModel)
                                Total += dataModel!!.Price.toDouble()
                            }
                            else if(tag_ == "Flight")
                            {
                                val dataModel= document.toObject(FlightTicket::class.java)
                                dataModelList.add(dataModel)
                                Total += dataModel!!.Price.toDouble()
                            }
                        }
                        Total *= NumberOfSeat.get(0).toString().toInt()
                        TvTotal.setText("Tổng tiền: "+ formatter(Total.toInt()).toString() + " VND")
                    }
                    .addOnFailureListener{exception ->
                        Log.w("Error getting documents: ", exception)
                    }
            }
            PayBtn.setOnClickListener {
                val Invoice_Id = generateRandomString(14)
                val Service_Invoice_Id = tag_[0] + generateRandomString(14)
                val new_invoice = invoice(Invoice_Id, tag_, user_id.toString(), NumberOfSeat?.get(0).toString(), Total.toString())
                var new_service_invoice = service_invoice(FirstID.toString(),SecondID.toString(), Service_Invoice_Id, Invoice_Id)
                var BusTicketList: ArrayList<BusTicket> = ArrayList()
                var FlightTicketList: ArrayList<FlightTicket> = ArrayList()
                if (tag_ == "Bus")
                {
                    BusTicketList = ArrayList(dataModelList.filterIsInstance<BusTicket>())
                    for ( i in BusTicketList)
                    {
                        var numseat = i.NumSeat.toDouble()
                        numseat -= NumberOfSeat!!.get(0).toString().toInt()
                        i.NumSeat = numseat.toString()
                        databaseReference.collection(tag_).document(i.Id).update("NumSeat", i.NumSeat)
                    }
                }
                else if (tag_ == "Flight")
                {
                    FlightTicketList = ArrayList(dataModelList.filterIsInstance<FlightTicket>())
                    for ( i in FlightTicketList)
                    {
                        i.NumSeat -= NumberOfSeat!!.get(0).toString().toInt()
                        databaseReference.collection(tag_).document(i.Id).update("NumSeat", i.NumSeat)
                    }
                }
                databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice)
                databaseReference.collection(tag_+"_invoice").document(Service_Invoice_Id).set(new_service_invoice)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                    }
                Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()

            }
        }
        else if (tag_ == "Hotel")
        {
            val RoomID = this.arguments?.getString("SelectedID")
            val DayStart = this.arguments?.getString("DayStart")
            var PayBtn: Button = rootView.findViewById<Button>(R.id.PayBtn)
            val DayEnd = this.arguments?.getString("DayEnd")
            val NumRoom = this.arguments?.getString("NumRoom")
            var room_ = Room()
            var hotel_ = Hotel()
            var Total: Double = 0.0
            databaseReference.collection("Room").document(RoomID.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null)
                    {
                        room_ = document.toObject(Room::class.java)!!
                        val dateFormatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val from = LocalDate.parse(DayStart.toString(), dateFormatter)
                        val to = LocalDate.parse(DayEnd.toString(), dateFormatter)
                        val period = Period.between(from, to)
                        val days = period.days
                        Total = (room_.Price.toInt()*days.toInt()).toDouble()
                        room_.State = "Rented"
                        TvTotal.setText("Tổng tiền: "+ formatter(room_.Price.toInt()*days.toInt()).toString() + " VND")
                    }
                }
                .addOnFailureListener{exception ->
                    Log.e("TAG", "User data not found")
                }
            PayBtn.setOnClickListener {
                val Invoice_Id = generateRandomString(14)
                val Service_Invoice_Id = tag_[0] + generateRandomString(14)
                val new_invoice = invoice(Invoice_Id, tag_, user_id.toString(), NumRoom?.get(0).toString(), Total.toString())
                var new_service_invoice = hotel_invoice(DayStart.toString(),DayEnd.toString(), Service_Invoice_Id, Invoice_Id, RoomID.toString())
                databaseReference.collection("Room").document(room_.Id).update("State", room_.State)
                databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice)
                databaseReference.collection(tag_+"_invoice").document(Service_Invoice_Id).set(new_service_invoice)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                    }
                Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()

            }
        }
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}