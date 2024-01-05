package com.example.nt118_project.Model

class ServiceCar_Ticket {
    public var id:String = ""
    public var Place: String = ""
    public var CarName: String = ""
    public var Status: String = ""
    public var DepatureTime : String = ""
    public var EndTime: String = ""
    public var NumSeat:Int = 0
    public var NumLuggage: Int = 0
    public var Price: Int = 0
    constructor(){}
    constructor(Id:String,
                DepaturePlace:String,
                Company:String,
                Status:String,
                TimePick:String,
                DepatureTime:String,
                EndTime:String,
                NumSeat: Int,
                NumLuggage: Int,
                Price:Int)
    {
        this.id = Id
        this.Place = DepaturePlace
        this.CarName = Company
        this.Status = Status
        this.DepatureTime =DepatureTime
        this.EndTime = EndTime
        this.NumLuggage = NumLuggage
        this.NumSeat = NumSeat
        this.Price = Price
    }

    fun setID(id:String)
    {
        this.id = id
    }
    fun getID(): String {return this.id}
}