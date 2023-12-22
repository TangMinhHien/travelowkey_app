package com.example.nt118_project.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val FirstID = this.arguments?.getString("FirstID")
        val SecondID = this.arguments?.getString("SecondID")
        val NumberOfSeat = this.arguments?.getString("Seat")
        var words:List<String> = NumberOfSeat.toString().split("\\s+".toRegex())
        var Total:Double = 0.0
        val rootView = inflater.inflate(R.layout.fragment_payment_info, container, false)
        var PayBtn: Button = rootView.findViewById<Button>(R.id.PayBtn)
        val databaseReference = Firebase.firestore
        if (NumberOfSeat != null) {
            databaseReference.collection("Bus").whereIn("Id", listOf(FirstID,SecondID))
                .get()
                .addOnSuccessListener { documents ->
                    var TvTotal: TextView = rootView.findViewById<TextView>(R.id.tVTotal)
                    for (document in documents)
                    {
                        val dataModel= document.toObject(BusTicket::class.java)
                        Total += dataModel!!.Price.toDouble()
                    }
                    Total *= words[0].toDouble()
                    TvTotal.setText("Tổng tiền: "+ Total.toInt().toString() + " VND")
                }
                .addOnFailureListener{exception ->
                    Log.w("Error getting documents: ", exception)
                }
        }
        PayBtn.setOnClickListener {
            data class bus_invoice(
                val Bus_Id_1:String,
                val Bus_Id_2: String,
                val Id: String,
                val Invoice_Id: String
            )
            data class invoice(
                val Id: String,
                val Tag: String,
                val User_Id: String,
                val Num_Ticket: String,
                val Total: String
            )

            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val user_id = currentUser!!.uid
            val Invoice_Id = generateRandomString(14)
            val Bus_Invoice_Id_1 = "B" + generateRandomString(14)
            val new_invoice = invoice(Invoice_Id, "Bus", user_id.toString(), NumberOfSeat.toString(), Total.toString())
            val new_bus_invoice_1 = bus_invoice(FirstID.toString(),SecondID.toString(), Bus_Invoice_Id_1, Invoice_Id)
            val databaseReference = Firebase.firestore
//            if(SecondID.toString() != "")
//            {
//                val Bus_Invoice_Id_2 = "B" + generateRandomString(14)
//                val new_bus_invoice_2 = bus_invoice(SecondID.toString(), Bus_Invoice_Id_2, Invoice_Id)
//                databaseReference.collection("Bus_invoice").document(Bus_Invoice_Id_2).set(new_bus_invoice_2)
//                    .addOnSuccessListener {
//                    }.addOnFailureListener { e ->
//                        Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
//                    }
//            }
            databaseReference.collection("Bus").whereIn("Id", listOf(FirstID,SecondID))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents)
                    {
                        val dataModel= document.toObject(BusTicket::class.java)
                        var numseat = dataModel.NumSeat.toDouble()
                        numseat -= words[0].toDouble()
                        dataModel.NumSeat = numseat.toString()
                        databaseReference.collection("Bus").document(document.id).update("NumSeat", dataModel.NumSeat)
                    }
                }
                .addOnFailureListener{exception ->
                    Log.w("Error getting documents: ", exception)
                }
            databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice)
            databaseReference.collection("Bus_invoice").document(Bus_Invoice_Id_1).set(new_bus_invoice_1)
                .addOnSuccessListener {
                    Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                }
            Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()

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