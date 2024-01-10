package com.example.nt118_project.Model

import java.util.Objects


class Notification {
    public var Id: String = ""
    public var Text: String = ""
    public var Tag: String = ""
    public var State: String = ""
    public var User_Id: String = ""
    public var Created: String = ""

    constructor() {}
    constructor(Id: String, Text: String,Tag:String, State: String, User_Id: String, Created: String)
    {
        this.Id = Id
        this.Text = Text
        this.Tag = Tag
        this.State = State
        this.User_Id = User_Id
        this.Created = Created
    }
}