package com.example.nt118_project.Model

class BusTicket {
    public var Name: String = ""
    public var NumberOfSeat: String = ""
    public var DepartureTime: String = ""
    public var ArrivalTime: String = ""
    public var TravelTime: String = ""
    public var DeparturePoint: String = ""
    public var ArrivalPoint: String = ""
    public var DeparturePlace: String = ""
    public var ArrivalPlace: String = ""
    public var Price: String = ""
    public var NumberOfCus:String = "1"
    public var DepartureDate:String = ""
    private var id:String = ""
    constructor(){}
    constructor(ArrivalPlace:String,
                ArrivalPoint:String,
                ArrivalTime:String,
                DeparturePlace:String,
                DeparturePoint:String,
                DepartureTime:String,
                Name:String,
                NumberOfCus:String,
                NumberOfSeat:String,
                Price:String,
                TravelTime:String,
                DepartureDate:String)
    {
        this.Name = Name
        this.NumberOfSeat = NumberOfSeat
        this.DepartureTime = DepartureTime
        this.ArrivalTime = ArrivalTime
        this.TravelTime = TravelTime
        this.DeparturePoint = DeparturePoint
        this.ArrivalPoint = ArrivalPoint
        this.DeparturePlace = DeparturePlace
        this.ArrivalPlace = ArrivalPlace
        this.Price = Price
        this.DepartureDate = DepartureDate
    }
    fun setID(id:String)
    {
        this.id = id
    }
    fun getID(): String {return this.id}
}