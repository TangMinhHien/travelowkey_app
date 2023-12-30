package com.example.nt118_project.Model

class BusTicket {
    public var ArrivalTime: String = ""
    public var Date: String = ""
    public var DepartureTime: String = ""
    public var DesPoint: String = ""
    public var From: String = ""
    public var Id: String = ""
    public var Name: String = ""
    public var NumSeat: String = ""
    public var PickPoint: String = ""
    public var Price: Int = 0
    public var To:String = ""
    public var TravelTime:String = ""
    public var Type:String = ""
    constructor(){}
    constructor(ArrivalTime: String,
            Date: String,
            DepartureTime: String,
            DesPoint: String,
            From: String,
            Id: String,
            Name: String,
            NumSeat: String,
            PickPoint: String,
            Price: Int,
            To:String,
            TravelTime:String,
            Type:String)
    {
        this.ArrivalTime = ArrivalTime
        this.Date = Date
        this.DepartureTime = DepartureTime
        this.DesPoint = DesPoint
        this.From = From
        this.Id = Id
        this.Name = Name
        this.NumSeat = NumSeat
        this.PickPoint = PickPoint
        this.Price = Price
        this.To = To
        this.TravelTime = TravelTime
        this.Type = Type
    }

}