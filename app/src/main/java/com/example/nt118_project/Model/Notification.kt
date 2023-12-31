package com.example.nt118_project.Model

class Notification {
    public var Id: String = ""
    public var Text: String = ""
    public var Tag: String = ""
    public var State: String = ""

    constructor() {}
    constructor(Id: String, Text: String,Tag:String, State: String)
    {
        this.Id = Id
        this.Text = Text
        this.Tag = Tag
        this.State = State
    }
}