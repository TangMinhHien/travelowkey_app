package com.example.nt118_project.Model

class HotelTicketInvoice {
    public lateinit var RoomID: String
    public lateinit var ChechOutDate: String
    public lateinit var ChechInDate: String
    public lateinit var Price: String
    public lateinit var NumCus: String
    constructor(){}
    constructor(RoomID: String, ChechOutDate: String, ChechInDate: String, NumCus:String, Price:String)
    {
        this.RoomID = RoomID
        this.ChechOutDate = ChechOutDate
        this.ChechInDate = ChechInDate
        this.NumCus = NumCus
        this.Price = Price
    }
}