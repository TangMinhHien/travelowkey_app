package com.example.nt118_project.Model

class Point {
    public var Invoice_Id: String = ""
    public var Text: String = ""
    public var Type: String = ""
    public var User_Id: String = ""
    public var id: String = ""
    public var points:String = ""
//    public var timestamp:Long = 0L

    constructor() {}
    constructor(Invoice_Id: String, Text: String,Type:String, User_Id: String, id:String, points: String)
    {
        this.Invoice_Id = Invoice_Id
        this.Text = Text
        this.Type = Type
        this.User_Id = User_Id
        this.id = id
        this.points = points
//        this.timestamp = timestamp
    }
}