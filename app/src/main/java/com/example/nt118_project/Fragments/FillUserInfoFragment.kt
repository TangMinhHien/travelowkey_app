package com.example.nt118_project.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.autofill.Validators.and
import android.service.autofill.Validators.or
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nt118_project.Adapter.BusTicketAdapter
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FillUserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FillUserInfoFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_fill_user_info, container, false)
        val databaseReference = Firebase.firestore
        databaseReference.collection("Bus").whereIn("Id", listOf(FirstID,SecondID))
            .get()
            .addOnSuccessListener { documents ->
                var dataList:ArrayList<BusTicket> = ArrayList<BusTicket>().apply {
                    add(BusTicket())
                }
                var recyclerViewBusTicket: RecyclerView = rootView.findViewById<RecyclerView>(R.id.RecyclerViewBusTicket)
                for (document in documents)
                {
                    val dataModel= document.toObject(BusTicket::class.java)
                    if(dataModel.Id == FirstID)
                        dataList[0] = dataModel
                    else if(dataModel.Id == SecondID)
                        dataList.add(dataModel)
                }
                var busTicketAdapter = BusTicketAdapter(dataList)
                recyclerViewBusTicket.adapter = busTicketAdapter
                val context: Context = requireActivity()
                recyclerViewBusTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            }
            .addOnFailureListener{exception ->
                Log.w("Error getting documents: ", exception)
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
         * @return A new instance of fragment FillUserInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FillUserInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}