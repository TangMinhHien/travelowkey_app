package com.example.nt118_project.Model

class Notification {
    public var Id: String = ""
    public var Text: String = ""
    public var Tag: String = ""
    public var State: String = ""
    public var User_Id: String = ""

    constructor() {}
    constructor(Id: String, Text: String,Tag:String, State: String, User_Id: String)
    {
        this.Id = Id
        this.Text = Text
        this.Tag = Tag
        this.State = State
        this.User_Id = User_Id
    }
}