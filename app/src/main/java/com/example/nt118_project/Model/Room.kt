package com.example.nt118_project.Model

class Room {
    public var Id: String = ""
    public var Hotel_id: String = ""
    public var Name: String = ""
    public var Max: Int = 1
    public var State: String = ""
    public var Price: Int = 0
    public var Service: String = ""
    constructor(){}
    constructor(Id:String,HotelId:String,Name:String,Max:Int,Service:String,State:String,Price:Int){
        this.Id = Id
        this.Hotel_id = HotelId
        this.Name = Name
        this.Max = Max
        this.Service = Service
        this.State = State
        this.Price = Price
    }
}