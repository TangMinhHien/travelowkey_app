package com.example.nt118_project.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.nt118_project.MainActivity
import com.example.nt118_project.Model.BusTicket
import com.example.nt118_project.Model.Coupon
import com.example.nt118_project.Model.FlightTicket
import com.example.nt118_project.Model.Hotel
import com.example.nt118_project.Model.Notification
import com.example.nt118_project.Model.Point
import com.example.nt118_project.Model.Room
import com.example.nt118_project.Model.User
import com.example.nt118_project.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
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
        var iVQRcode: ImageView = rootView.findViewById(R.id.QRcode)
        val tVNotiPoint: TextView = rootView.findViewById(R.id.tVNotiPoint)
        val databaseReference = Firebase.firestore

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val user_id = currentUser!!.uid

        val tag_ = this.arguments?.getString("Tag")
        class UsedCoupon {
            public lateinit var id: String
            public lateinit var user_Id: String
            public lateinit var coupon_Id: ArrayList<String>
            constructor(){}
            constructor(coupon_Id:ArrayList<String>, id:String, user_Id:String)
            {
                this.id=id
                this.user_Id=user_Id
                this.coupon_Id=coupon_Id
            }
        }
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
        val usersRef = Firebase.firestore
        var user_ = User()
        var Total_:Double = 0.0
        var PointValue:Int = 0
        var CouponValue:Int = 0
        var LastTotal: Int = 0
        var globalCoupon_:Coupon = Coupon()
        var globalUsedCoupon_:UsedCoupon = UsedCoupon()
        val tVPoint:TextView = rootView.findViewById(R.id.tVPoint)
        val switchButton: SwitchMaterial = rootView.findViewById(R.id.material_switch)
        val switchButton_coupon: SwitchMaterial = rootView.findViewById(R.id.material_switch_coupon)
        val CodeCoupon:EditText = rootView.findViewById(R.id.tVCoupon)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val strcurrentDay = LocalDateTime.now().format(formatter)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDay: Date = sdf.parse(strcurrentDay)

        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            // Handle the switch state change
            if (isChecked) {
                // Switch is ON
                if(LastTotal <= 50000)
                {
                    Toast.makeText(requireActivity(), "Tiền thanh toán tối thiểu là 50.000 VND", Toast.LENGTH_SHORT).show()
                    PointValue = 0
                    switchButton.isChecked = false
                }
                else{
                    var redeemedPoint:Int  = user_.Point
                    var temp_redeemedPoint:Int = ((Total_ - 50000)*0.1).toInt()
                    if (temp_redeemedPoint <= redeemedPoint)
                    {
//                        tVPoint.text = temp_redeemedPoint.toString() + " điểm"
                        PointValue = temp_redeemedPoint * 10
                    }
                    else {
                        PointValue = redeemedPoint * 10
//                        tVPoint.text = redeemedPoint.toString() + " điểm"
                    }
                    if(LastTotal - PointValue < 50000)
                    {
                        Toast.makeText(requireActivity(), "Tiền thanh toán tối thiểu là 50.000 VND", Toast.LENGTH_SHORT).show()
                        PointValue = 0
                        switchButton.isChecked = false
                    }
                    else
                    {
                        LastTotal -= PointValue
                        TvTotal.setText("Tổng tiền: "+ formatter(LastTotal).toString() + " VND")
                        tVNotiPoint.text = "Bạn nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
                    }
                }
            } else {
                // Switch is OFF
                LastTotal += PointValue
                TvTotal.setText("Tổng tiền: "+ formatter(LastTotal).toString() + " VND")
//                LastTotal = Total_.toInt()
                tVNotiPoint.text = "Bạn nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
            }
        }

        switchButton_coupon.setOnCheckedChangeListener { buttonView, isChecked ->
            // Handle the switch state change
            if (isChecked) {
                // Switch is ON
                if(CodeCoupon.text.toString() == "")
                {
                    Toast.makeText(requireActivity(), "Vui lòng điền mã giảm giá", Toast.LENGTH_SHORT).show()
                    CouponValue = 0
                    switchButton_coupon.isChecked = false
                }
                else if(LastTotal == 50000)
                {
                    Toast.makeText(requireActivity(), "Tiền thanh toán tối thiểu là 50.000 VND", Toast.LENGTH_SHORT).show()
                    CouponValue = 0
                    switchButton_coupon.isChecked = false
                }
                else
                {
                    usersRef.collection("UsedCoupon").whereEqualTo("user_Id", user_id).get().addOnSuccessListener{documents ->
                        if(documents.size() == 0)
                        {
                            //Toast.makeText(requireActivity(), "Mã giảm giá không tồn tại", Toast.LENGTH_SHORT).show()
                            switchButton_coupon.isChecked = false
                        }
                        else
                        {
                            for (document in documents)
                            {
                                val usedcoupon_ = document.toObject(UsedCoupon::class.java)
                                usersRef.collection("Coupon").whereEqualTo("Code",CodeCoupon.text.toString()).get().addOnSuccessListener { documents->
                                    if(documents.size() == 0)
                                    {
                                        Toast.makeText(requireActivity(), "Mã giảm giá không tồn tại", Toast.LENGTH_SHORT).show()
                                        switchButton_coupon.isChecked = false
                                    }
                                    else{
                                        for(document in documents)
                                        {
                                            val coupon_ = document.toObject(Coupon::class.java)
                                            if(!currentDay.before(sdf.parse(coupon_.ExpiryDate)))
                                            {
                                                Toast.makeText(requireActivity(), "Mã giảm giá này đã hết hạn sử dụng", Toast.LENGTH_SHORT).show()
                                                CouponValue = 0
                                                switchButton_coupon.isChecked = false
                                                continue
                                            }
                                            if(coupon_.Tag != tag_)
                                            {
                                                Toast.makeText(requireActivity(), "Mã giảm giá này không thể áp dụng cho dịch vụ này", Toast.LENGTH_SHORT).show()
                                                CouponValue = 0
                                                switchButton_coupon.isChecked = false
                                                continue
                                            }
                                            if(coupon_.Max < 1)
                                            {
                                                Toast.makeText(requireActivity(), "Số lượng mã giảm giá này đã hết", Toast.LENGTH_SHORT).show()
                                                CouponValue = 0
                                                switchButton_coupon.isChecked = false
                                                continue
                                            }
                                            if(!coupon_.Value.contains("%"))
                                            {
                                                if(LastTotal - coupon_.Value.toInt()<50000)
                                                {
                                                    Toast.makeText(requireActivity(), "Tiền thanh toán tối thiểu là 50.000 VND", Toast.LENGTH_SHORT).show()
                                                    CouponValue = 0
                                                    switchButton_coupon.isChecked = false
                                                    continue
                                                }
                                            }
                                            else
                                            {
                                                val temp:Int = Total_.toInt()
                                                val temp_value = (coupon_.Value.subSequence(0, coupon_.Value.length - 1)).toString().toDouble() * 0.01
                                                if(LastTotal - (temp_value  * temp).toInt() < 50000)
                                                {
                                                    Toast.makeText(requireActivity(), "Tiền thanh toán tối thiểu là 50.000 VND 1", Toast.LENGTH_SHORT).show()
                                                    CouponValue = 0
                                                    switchButton_coupon.isChecked = false
                                                    continue
                                                }
                                            }
                                            if(coupon_.id in usedcoupon_.coupon_Id)
                                            {
                                                Toast.makeText(requireActivity(), "Bạn đã sử dụng mã giảm giá này", Toast.LENGTH_SHORT).show()
                                                CouponValue = 0
                                                switchButton_coupon.isChecked = false
                                                continue
                                            }
                                            if(coupon_.Value.contains("%"))
                                            {
                                                val temp:Int = Total_.toInt()
                                                val temp_value = (coupon_.Value.subSequence(0, coupon_.Value.length - 1)).toString().toDouble() * 0.01
                                                LastTotal -= (temp_value  * temp).toInt()
                                                CouponValue = (temp_value  * temp).toInt()
                                            }
                                            else
                                            {
                                                LastTotal -= coupon_.Value.toInt()
                                                CouponValue = coupon_.Value.toInt()
                                            }
                                            globalUsedCoupon_ = usedcoupon_
                                            globalCoupon_ = coupon_
                                            TvTotal.setText("Tổng tiền: "+ formatter(LastTotal).toString() + " VND")
                                            tVNotiPoint.text = "Bạn sẽ nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Switch is OFF
                LastTotal += CouponValue
                TvTotal.setText("Tổng tiền: "+ formatter(LastTotal).toString() + " VND")
                tVNotiPoint.text = "Bạn sẽ nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
            }
        }

        CodeCoupon.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Executed before the text is changed
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Executed as the text is changed
//                if(switchButton.isChecked == false)
//                    CouponValue = 0
//                switchButton_coupon.isChecked = false
            }

            override fun afterTextChanged(editable: Editable?) {
                // Executed after the text has changed
                // val newText = editable.toString()
                // Do something with the new text
                if(switchButton_coupon.isChecked == false)
                    CouponValue = 0
                switchButton_coupon.isChecked = false
            }
        })

        usersRef.collection("User").document(user_id).get()
            .addOnSuccessListener { document ->
                if (document != null)
                {
                    user_ = document.toObject(User::class.java)!!
                }
            }
            .addOnFailureListener{exception ->
                Log.e("TAG", "User data not found")
            }

        if(tag_ == "Bus" || tag_ == "Flight")
        {
            val FirstID = this.arguments?.getString("FirstID")
            val SecondID = this.arguments?.getString("SecondID")
            val NumberOfSeat = this.arguments?.getString("Seat")
            val Invoice_Id = generateRandomString(14)

            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(Invoice_Id, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width)
                {
                    for(y in 0 until height)
                    {
                        bmp.setPixel(x,y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                iVQRcode.setImageBitmap(bmp)
            }catch(e: WriterException){
                e.printStackTrace()
            }

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
                        Total_ = Total
                        LastTotal = Total_.toInt()
                        TvTotal.setText("Tổng tiền: "+ formatter(Total.toInt()).toString() + " VND")
                        var redeemedPoint:Int  = user_.Point
                        var temp_redeemedPoint:Int = ((Total - 50000)*0.1).toInt()
                        if (temp_redeemedPoint <= redeemedPoint)
                        {
                            tVPoint.text = temp_redeemedPoint.toString() + " điểm"
                            PointValue = temp_redeemedPoint * 10
                        }
                        else {
                            PointValue = redeemedPoint * 10
                            tVPoint.text = redeemedPoint.toString() + " điểm"
                        }
                        tVNotiPoint.text = "Bạn sẽ nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
                    }
                    .addOnFailureListener{exception ->
                        Log.w("Error getting documents: ", exception)
                    }
            }
            PayBtn.setOnClickListener {
                val Service_Invoice_Id = tag_[0] + generateRandomString(14)
                val new_invoice = invoice(Invoice_Id, tag_, user_id.toString(), NumberOfSeat?.get(0).toString(), LastTotal.toString())
                var new_service_invoice = service_invoice(FirstID.toString(),SecondID.toString(), Service_Invoice_Id, Invoice_Id)
                var BusTicketList: ArrayList<BusTicket> = ArrayList()
                var FlightTicketList: ArrayList<FlightTicket> = ArrayList()
                val noti_list: ArrayList<String> = ArrayList()
                var textaccumulatedpoint:String = ""
                var textredeemedpoint:String = ""
                if(switchButton.isChecked){
                    user_.Point += ((LastTotal * 0.008).toInt() - (PointValue * 0.1).toInt())
                }
                else
                    user_.Point += ((LastTotal * 0.008).toInt())
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val currentDate = LocalDateTime.now().format(formatter)
                if (tag_ == "Bus")
                {
                    textaccumulatedpoint = "Bạn nhận được điểm khi thanh toán hóa đơn vé xe khách (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                    textredeemedpoint = "Bạn đã sử dụng điểm khi thanh toán hóa đơn vé xe khách (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val inputFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                    BusTicketList = ArrayList(dataModelList.filterIsInstance<BusTicket>())
                    for ( i in BusTicketList)
                    {
                        var numseat = i.NumSeat.toDouble()
                        numseat -= NumberOfSeat!!.get(0).toString().toInt()
                        i.NumSeat = numseat.toString()
                        databaseReference.collection(tag_).document(i.Id).update("NumSeat", i.NumSeat)
                        val date = inputFormat.parse(i.Date)
                        var Date= outputFormat.format(date!!)
                        var noti_text = "Bạn đã đăng ký vé xe khách từ "+i.From+" đến "+i.To+" vào ngày "+Date+", chuyến xe mang mã số "+i.Id+"."
                        noti_list.add(noti_text)
                    }
                }
                else if (tag_ == "Flight")
                {
                    textaccumulatedpoint = "Bạn nhận được điểm khi thanh toán hóa đơn vé máy bay (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                    textredeemedpoint = "Bạn đã sử dụng điểm khi thanh toán hóa đơn vé máy bay (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                    FlightTicketList = ArrayList(dataModelList.filterIsInstance<FlightTicket>())
                    for ( i in FlightTicketList)
                    {
                        i.NumSeat -= NumberOfSeat!!.get(0).toString().toInt()
                        databaseReference.collection(tag_).document(i.Id).update("NumSeat", i.NumSeat)
                        var noti_text = "Bạn đã đăng ký vé máy bay từ "+i.From+" đến "+i.To+" vào ngày "+i.Date+", chuyến bay số hiệu "+i.Id+"."
                        noti_list.add(noti_text)
                    }
                }
                databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice).addOnSuccessListener {
                    databaseReference.collection(tag_+"_invoice").document(Service_Invoice_Id).set(new_service_invoice)
                        .addOnSuccessListener {
                            val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Notification")
                            val map: HashMap<String, Any> = HashMap<String, Any>()
                            map["timestamp"] = ServerValue.TIMESTAMP
                            for (noti in noti_list)
                            {
                                val noti_id= generateRandomString(14)
                                var new_noti: Notification = Notification(noti_id, noti, tag_, "Not", user_id)
                                dbRef.child(noti_id).setValue(new_noti)
                                dbRef.child(noti_id).updateChildren(map)
                            }
                            val dbRefPoint: DatabaseReference = FirebaseDatabase.getInstance().getReference("Point")
                            var new_accumulated_point = Point(Invoice_Id, textaccumulatedpoint,"accumulated", user_id.toString(), "P"+generateRandomString(14), (LastTotal * 0.008).toInt().toString())
                            dbRefPoint.child(new_accumulated_point.id).setValue(new_accumulated_point)
                            dbRefPoint.child(new_accumulated_point.id).updateChildren(map)
                            if(switchButton.isChecked)
                            {
                                var new_redeemed_point = Point(Invoice_Id, textredeemedpoint,"redeemed", user_id.toString(), "P"+generateRandomString(14), (PointValue * 0.1).toInt().toString())
                                dbRefPoint.child(new_redeemed_point.id).setValue(new_redeemed_point)
                                dbRefPoint.child(new_redeemed_point.id).updateChildren(map)
                            }
                            if(switchButton_coupon.isChecked)
                            {
                                databaseReference.collection("Coupon").document(globalCoupon_.id).update("Max", globalCoupon_.Max - 1)
                                val listcoupon: ArrayList<String> = globalUsedCoupon_.coupon_Id
                                listcoupon.add(globalCoupon_.id)
                                Log.d("listcoupon", listcoupon.size.toString())
                                databaseReference.collection("UsedCoupon").document(globalUsedCoupon_.id).update("coupon_Id", listcoupon)
                            }
                            databaseReference.collection("User").document(user_id).update("point", user_.Point)
                                .addOnSuccessListener {
                                    Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()
                                    val intent = Intent(requireActivity(), MainActivity::class.java)
                                    val LAUNCH_SECOND_ACTIVITY:Int = 1
                                    startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                                }
                        }.addOnFailureListener { e ->
                            Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                        }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                }
            }
        }
        else if (tag_ == "Hotel")
        {
            val RoomID = this.arguments?.getString("SelectedID")
            val DayStart = this.arguments?.getString("DayStart")
            var PayBtn: Button = rootView.findViewById<Button>(R.id.PayBtn)
            val DayEnd = this.arguments?.getString("DayEnd")
            val NumRoom = this.arguments?.getString("NumRoom")
            val noti_list: ArrayList<String> = ArrayList()
            var room_ = Room()
            var hotel_ = Hotel()
            var Total: Double = 0.0
            val Invoice_Id = generateRandomString(14)
            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(Invoice_Id, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width)
                {
                    for(y in 0 until height)
                    {
                        bmp.setPixel(x,y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                iVQRcode.setImageBitmap(bmp)
            }catch(e: WriterException){
                e.printStackTrace()
            }

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
                        Total_ = Total
                        LastTotal = Total_.toInt()
                        room_.State = "Rented"
                        TvTotal.setText("Tổng tiền: "+ formatter(room_.Price.toInt()*days.toInt()).toString() + " VND")
                        var redeemedPoint:Int  = user_.Point
                        var temp_redeemedPoint:Int = ((Total - 50000)*0.1).toInt()
                        if (temp_redeemedPoint <= redeemedPoint)
                        {
                            tVPoint.text = temp_redeemedPoint.toString() + " điểm"
                            PointValue = temp_redeemedPoint * 10
                        }
                        else
                        {
                            PointValue = redeemedPoint * 10
                            tVPoint.text = redeemedPoint.toString() + " điểm"
                        }
                        tVNotiPoint.text = "Bạn sẽ nhận được "+(LastTotal * 0.008).toInt().toString()+" điểm khi thực hiện thanh toán."
                        databaseReference.collection("Hotel").document(room_.Hotel_id).get()
                            .addOnSuccessListener { document ->
                                hotel_ = document.toObject(Hotel::class.java)!!
                                var noti_text = "Bạn đã đăng ký phòng "+ room_.Name +" của khách sạn "+hotel_.Name+" từ ngày "+DayStart.toString()+" đến ngày "+DayEnd.toString()+"."
                                noti_list.add(noti_text)
                            }
                    }
                }
                .addOnFailureListener{exception ->
                    Log.e("TAG", "User data not found")
                }
            PayBtn.setOnClickListener {
                val Service_Invoice_Id = tag_[0] + generateRandomString(14)
                val new_invoice = invoice(Invoice_Id, tag_, user_id.toString(), NumRoom?.get(0).toString(), LastTotal.toString())
                var new_service_invoice = hotel_invoice(DayStart.toString(),DayEnd.toString(), Service_Invoice_Id, Invoice_Id, RoomID.toString())
                databaseReference.collection("Room").document(room_.Id).update("State", room_.State)
//                databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice)
                if(switchButton.isChecked){
                    user_.Point += ((LastTotal * 0.008).toInt() - (PointValue * 0.1).toInt())
                }
                else
                    user_.Point += ((LastTotal * 0.008).toInt())
                databaseReference.collection("Invoice").document(Invoice_Id).set(new_invoice).addOnSuccessListener {
                    databaseReference.collection(tag_+"_invoice").document(Service_Invoice_Id).set(new_service_invoice)
                        .addOnSuccessListener {
                            var textaccumulatedpoint:String = ""
                            var textredeemedpoint:String = ""
                            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                            val currentDate = LocalDateTime.now().format(formatter)
                            textaccumulatedpoint = "Bạn nhận được điểm khi thanh toán hóa đơn đặt phòng khách sạn (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                            textredeemedpoint = "Bạn đã sử dụng điểm khi thanh toán hóa đơn đặt phòng khách sạn (mã hóa đơn "+ Invoice_Id +") ngày " + currentDate +"."
                            val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Notification")
                            val map: HashMap<String, Any> = HashMap<String, Any>()
                            map["timestamp"] = ServerValue.TIMESTAMP
                            for (noti in noti_list)
                            {
                                val noti_id= generateRandomString(14)
                                var new_noti: Notification = Notification(noti_id, noti, tag_, "Not", user_id)
                                dbRef.child(noti_id).setValue(new_noti)
                                dbRef.child(noti_id).updateChildren(map)
                            }
                            val dbRefPoint: DatabaseReference = FirebaseDatabase.getInstance().getReference("Point")
                            var new_accumulated_point = Point(Invoice_Id, textaccumulatedpoint,"accumulated", user_id.toString(), "P"+generateRandomString(14), (LastTotal * 0.008).toInt().toString())
                            dbRefPoint.child(new_accumulated_point.id).setValue(new_accumulated_point)
                            dbRefPoint.child(new_accumulated_point.id).updateChildren(map)
                            if(switchButton.isChecked)
                            {
                                var new_redeemed_point = Point(Invoice_Id, textredeemedpoint,"redeemed", user_id.toString(), "P"+generateRandomString(14), (PointValue * 0.1).toInt().toString())
                                dbRefPoint.child(new_redeemed_point.id).setValue(new_redeemed_point)
                                dbRefPoint.child(new_redeemed_point.id).updateChildren(map)
                            }
                            if(switchButton_coupon.isChecked)
                            {
                                databaseReference.collection("Coupon").document(globalCoupon_.id).update("Max", globalCoupon_.Max - 1)
                                val listcoupon: ArrayList<String> = globalUsedCoupon_.coupon_Id
                                listcoupon.add(globalCoupon_.id)
                                Log.d("listcoupon", listcoupon.size.toString())
                                databaseReference.collection("UsedCoupon").document(globalUsedCoupon_.id).update("coupon_Id", listcoupon)
                            }
                            databaseReference.collection("User").document(user_id).update("point", user_.Point).addOnSuccessListener {
                                Toast.makeText(requireActivity(), "Thanh toán thành công", Toast.LENGTH_LONG).show()
                                val intent = Intent(requireActivity(), MainActivity::class.java)
                                val LAUNCH_SECOND_ACTIVITY:Int = 1
                                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(requireActivity(), "Thanh toán thất bại", Toast.LENGTH_LONG).show()
                        }
                }

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