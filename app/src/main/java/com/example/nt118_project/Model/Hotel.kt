package com.example.nt118_project.Model

class Hotel {
    public var Id:String = ""
    public var Name:String = ""
    public var Address:String = ""
    public var Price: Int = 0
    public var Rating: Int = 0
    public var Area:String = ""
    public var Img: String = ""
    constructor(){}
    constructor(Id:String,Name:String,Address:String,Area:String,Rating:Int,Price:Int,Img:String){
        this.Id=Id
        this.Name = Name
        this.Address = Address
        this.Area = Area
        this.Rating = Rating
        this.Price = Price
        this.Img = Img
    }
//    fun setID(Id:String){
//        this.Id = Id
//    }
//    fun getID():String{
//        return this.Id
//    }
}