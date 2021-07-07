package com.powellapps.buyforme.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Product : Serializable{

    var amount = 1
    var name = ""
    var purchased = false
    var shoppingId = ""
    var userId = ""
    @DocumentId
    var documentId = ""
    var referenceId = ""
    var lastUpdate : Long = 0
    var prices = arrayListOf<Double>()
    var purchases = arrayListOf<Purchase>()

    constructor(){
        lastUpdate = Date().time
    }

    constructor(name: String){
        this.name = name
        lastUpdate = Date().time
    }

    constructor(name: String, amount: Int = 1){
        this.name = name
        this.amount = amount
        lastUpdate = Date().time
    }

    constructor(name: String, amount: String){
        this.name = name
        this.amount = amount.toInt()
        this.lastUpdate = Date().time
    }

    fun nameMap() : HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put("name", name)
        return map
    }

    fun purchasedMap() : HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("purchased", purchased)
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + name.hashCode()
        return result
    }

    fun bestPrice(): Double {
        return try{
            purchases.sortedBy { it.price }[0].price
        }catch (e : Exception){
            0.0
        }
    }

}