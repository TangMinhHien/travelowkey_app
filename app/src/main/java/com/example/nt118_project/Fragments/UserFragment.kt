package com.example.nt118_project.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nt118_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
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
    class user{
        public var Id:String = ""
        public var Email: String = ""
        public var PhoneNumber: String = ""
        public var UserName: String = ""
        public var Address: String = ""
        public var Sex: String = ""
        public var Name: String = ""
        public var Birthday: String = ""
        public var Point:Int = 0

        constructor()
        constructor(id:String, email:String, phone:String, userName: String, address:String, sex:String, name:String, bd:String, point:Int)
        {
            this.Id = id
            this.Email = email
            this.PhoneNumber = phone
            this.UserName = userName
            this.Address = address
            this.Sex = sex
            this.Name = name
            this.Birthday = bd
            this.Point = point
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser_: FirebaseUser? = firebaseAuth.currentUser
        val currentUser = currentUser_!!.uid
        val usersRef = Firebase.firestore
        val tVuserName:TextView = view.findViewById(R.id.user_name)
        usersRef.collection("User").document(currentUser).get()
            .addOnSuccessListener { document ->
                var user_ = user()
                if (document != null)
                {
                    user_ = document.toObject(user::class.java)!!
                }
                tVuserName.text = user_.UserName
            }
            .addOnFailureListener{exception ->
                Log.e("TAG", "User data not found")
            }

        val avatar = view.findViewById<ImageView>(R.id.avatar)
        avatar.setOnClickListener {
            val intent = Intent(activity, UserDetailFragment::class.java)
            val pwStr = this.arguments?.getString("Pw")
            intent.putExtra("Pw", pwStr.toString())
            Log.w("PW", pwStr.toString())
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val icInfoUser = view.findViewById<ImageView>(R.id.ic_detailInfoUser)
        icInfoUser.setOnClickListener {
            val intent = Intent(activity, UserDetailFragment::class.java)
            val pwStr = this.arguments?.getString("Pw")
            intent.putExtra("Pw", pwStr.toString())
            Log.w("PW", pwStr.toString())
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val icPoint:ImageButton = view.findViewById<ImageButton>(R.id.ic_detailPoint)
        icPoint.setOnClickListener {
            val intent = Intent(activity,PointDetailActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val icCard:ImageButton = view.findViewById<ImageButton>(R.id.ic_detailCard)
        icCard.setOnClickListener {
            val intent = Intent(activity,DebitCardActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        val icCoupon:ImageButton = view.findViewById<ImageButton>(R.id.ic_detailCoupon)
        icCoupon.setOnClickListener {
            val intent = Intent(activity,CouponDetailActivity::class.java)
            val LAUNCH_SECOND_ACTIVITY:Int = 1
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
        }
        return view
    }


    fun onCreateView_(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}