package com.example.nt118_project.Model

import android.text.Editable

class User {
    public var Name: String = ""
    public var username: String = ""
    public var address: String = ""
    public var email: String = ""
    public var phone_number: String = ""
    public var sex: String = ""
    public var id:String = ""
    public var birthday:String = ""
    constructor()
    constructor(Name: String, username: String, address: String, email: String, phone_number: String, sex: String, id: String, birthday: String)
    {
        this.Name = Name
        this.username = username
        this.address = address
        this.email = email
        this.phone_number = phone_number
        this.sex = sex
        this.id = id
        this.birthday = birthday
    }
//    fun getUID():String {return this.id}
//    fun getUserName():String {return this.username}
//    fun getUserFullName():String {return this.Name}
//    fun getUserAddress():String {return this.address}
//    fun getUserEmail():String {return this.email}
//    fun getUserPhoneNumber():String {return this.phone_number}
//    fun getUserSex():String {return this.sex}
    //fun getUserBirthDay():String {return this.userBirthDay}
}