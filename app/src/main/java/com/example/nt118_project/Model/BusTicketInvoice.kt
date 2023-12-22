package com.example.nt118_project.Model

class BusTicketInvoice {
    public lateinit var FirstBusTicket_ID: String
    public lateinit var SecondBusTicket_ID: String
    public lateinit var Price: String
    public lateinit var NumCus: String
    constructor(){}
    constructor(FirstBusTicket_ID: String, SecondBusTicket_ID: String, NumCus:String, Price:String)
    {
        this.FirstBusTicket_ID = FirstBusTicket_ID
        this.SecondBusTicket_ID = SecondBusTicket_ID
        this.NumCus = NumCus
        this.Price = Price
    }
}