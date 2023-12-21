package com.example.nt118_project.Model

class FlightTicket {
    public var Id:String = ""
    public var Name: String = ""
    public var Date:String = ""
    public var DepartureTime: String = ""
    public var ArrivalTime: String = ""
    public var TravelTime: String = ""
    public var From : String = ""
    public var To: String = ""
    public var NumSeat:Int = 0
    public var Price: Int = 0
    public var Stop_Direct: String = ""
    public var SeatClass: String = ""
    constructor(){}
    constructor(Id:String,
                From:String,
                To:String,
                Date:String,
                DepartureTime:String,
                ArrivalTime:String,
                TravelTime:String,
                Stop_Direct:String,
                Name:String,
                SeatClass:String,
                NumSeat: Int,
                Price:Int)
    {
        this.Id = Id
        this.Name = Name
        this.From = From
        this.To = To
        this.Date = Date
        this.DepartureTime = DepartureTime
        this.ArrivalTime = ArrivalTime
        this.TravelTime = TravelTime
        this.Stop_Direct = Stop_Direct
        this.SeatClass = SeatClass
        this.NumSeat = NumSeat
        this.Price = Price
    }
//    public fun setID(Id:String){
//        this.Id = Id
//    }
//    public fun getID():String{
//        return this.Id
//    }
}