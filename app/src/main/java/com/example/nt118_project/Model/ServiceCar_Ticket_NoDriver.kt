package com.example.nt118_project.Model

class ServiceCar_Ticket_NoDriver {
    public var Id: String = ""
    public var type: String = ""
    public var Place: String = ""
    public var Name: String = ""
    public var Company: String = ""
    public var NumSeat: Int = 0
    public var NumLuggage: Int = 0
    public var Price: Int = 0
    public var image: String = ""
    constructor(){}
    constructor(Company: String,
                Id: String,
                Name: String,
                NumLuggage: Int,
                NumSeat: Int,
                Place :String,
                Price:Int,
                image: String,
                type: String)
    {
        this.Id = Id
        this.type = type
        this.Place = Place
        this.Company = Company
        this.Name = Name
        this.NumLuggage = NumLuggage
        this.NumSeat = NumSeat
        this.Price = Price
        this.image = image
    }

}