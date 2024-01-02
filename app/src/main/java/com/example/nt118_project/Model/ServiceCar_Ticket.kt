package com.example.nt118_project.Model

class ServiceCar_Ticket {
    public var Id:String = ""
    public var DepaturePlace: String = ""
    public var EndPlace: String = ""
    public var Company: String = ""
    public var Status: String = ""
    public var TimePick: String = ""
    public var DepatureTime : String = ""
    public var EndTime: String = ""
    public var NumSeat:Int = 0
    public var Price: Int = 0
    constructor(){}
    constructor(Id:String,
                DepaturePlace:String,
                EndPlace:String,
                Company:String,
                Status:String,
                TimePick:String,
                DepatureTime:String,
                EndTime:String,
                NumSeat: Int,
                Price:Int)
    {
        this.Id = Id
        this.DepaturePlace = DepaturePlace
        this.EndPlace = EndPlace
        this.Company = Company
        this.Status = Status
        this.TimePick = TimePick
        this.DepatureTime =DepatureTime
        this.EndTime = EndTime
        this.NumSeat = NumSeat
        this.Price = Price
    }
}