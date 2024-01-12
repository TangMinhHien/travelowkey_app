package com.example.nt118_project.Model

class Coupon {
    public var Code: String = ""
    public var ExpiryDate: String = ""
    public var Max: Int = 1
    public var Tag: String = ""
    public var Text: String = ""
    public var Value:String = ""
    public var id:String = ""
    constructor() {}
    constructor(Code: String, ExpiryDate: String, Max:Int, Tag: String, Text:String, Value: String, id:String)
    {
        this.Code = Code
        this.ExpiryDate = ExpiryDate
        this.Max = Max
        this.Tag = Tag
        this.Text = Text
        this.Value = Value
        this.id = id
    }
}