package com.powellapps.buyforme.model

import java.io.Serializable

class User : Serializable{
    var id: String = ""
    var name: String = ""
    var picture: String = ""
    var token = ""

    constructor(){}

    constructor(id: String, name: String, picture : String){
        this.id = id
        this.name = name
        this.picture = picture
    }

    override fun toString(): String {
        return name
    }
}