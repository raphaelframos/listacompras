package com.powellapps.buyforme.model

import java.io.Serializable
import java.util.*

class Purchase() : Serializable {

    var price: Double = 0.0
    lateinit var place : String
    var timestamp : Long = 0
    var observation = ""

    constructor(price: Double, place: String, observation : String) : this() {
        this.place = place
        this.price = price
        this.timestamp = Date().time
        this.observation = observation
    }
}