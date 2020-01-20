package com.powellapps.compraparamim.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable
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
    var prices = ArrayList<Double>()
    var referenceId = ""
    var currentPrice : Double = 0.0
    var date : Long = 0

    constructor(){
        date = Date().time
    }

    constructor(name: String){
        this.name = name
        date = Date().time
    }

    constructor(name: String, amount: Int){
        this.name = name
        this.amount = amount
        date = Date().time
    }

    fun map() : HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("name", name)
        map.put("amount", amount)
        map.put("purchased", purchased)
        return map
    }

    fun nameMap() : HashMap<String, String> {
        var map = HashMap<String, String>()
        map.put("name", name)
        return map
    }

    fun pricesMap(): HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("prices", prices)
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

    fun add(currentPrice: Double) {
        prices.add(currentPrice)
    }

    fun bestPrice(): Double {
        if(prices.size > 0 ) {
            return prices.min()!!
        }
        return 0.0
    }

    fun newPriceMap(): HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("purchased", purchased)
        map.put("currentPrice", currentPrice)
        map.put("prices", prices)
        return map
    }
}