package com.example.nt118_project.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("XeKhach")
        if (NumberOfSeat != null) {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var TvTotal: TextView = rootView.findViewById<TextView>(R.id.tVTotal)
                    for (snapshot in dataSnapshot.children) {
                        val dataModel: BusTicket? = snapshot.getValue(BusTicket::class.java)
                        val itemId:String = snapshot.key.toString()
                        if(itemId == FirstID)
                        {
                            Total += dataModel!!.Price.toDouble()
                        }
                        else if(itemId == SecondID)
                        {
                            Total += dataModel!!.Price.toDouble()
                        }
                    }
                    Total *= words[0].toDouble()
                    Total *= 1000
                    TvTotal.setText("Tổng tiền: "+ Total.toInt().toString() + " VND")
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
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