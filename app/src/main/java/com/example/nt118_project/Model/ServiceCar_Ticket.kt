package com.example.nt118_project.Model

class ServiceCar_Ticket {
    public var Id: String = ""
    public var type: String = ""
    public var Place: String = ""
    public var Name: String = ""
    public var Company: String = ""
    public var Status: String = ""
    public var NumSeat: Int = 0
    public var NumLuggage: Int = 0
    public var Price: Int = 0
    public var Image: String = ""
    constructor(){}
    constructor(Id: String,
                PickPoint :String,
                Name: String,
                Company: String,
                Status: String,
                type: String,
                NumSeat: Int,
                NumLuggage: Int,
                Price:Int,
                image: String)
    {
        this.Id = Id
        this.type = type
        this.Place = PickPoint
        this.Company = Company
        this.Name = Name
        this.Status = Status
        this.NumLuggage = NumLuggage
        this.NumSeat = NumSeat
        this.Price = Price
        this.Image = image
    }

    fun setID(id:String)
    {
        this.Id = id
    }
    fun getID(): String {return this.Id}
}