package com.example.nt118_project.Model

import android.text.Editable

class User{
    public var Id:String = ""
    public var Email: String = ""
    public var PhoneNumber: String = ""
    public var UserName: String = ""
    public var Address: String = ""
    public var Sex: String = ""
    public var Name: String = ""
    public var Birthday: String = ""
    public var Point:Int = 0

    constructor()
    constructor(id:String, email:String, phone:String, userName: String, address:String, sex:String, name:String, bd:String, point:Int)
    {
        this.Id = id
        this.Email = email
        this.PhoneNumber = phone
        this.UserName = userName
        this.Address = address
        this.Sex = sex
        this.Name = name
        this.Birthday = bd
        this.Point = point
    }
}