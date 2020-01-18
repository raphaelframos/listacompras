package com.powellapps.compraparamim.ui.newlist

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class Product : Serializable{

    var amount = 1
    var name = ""
    var purchased = false
    @DocumentId
    var documentId = ""

    constructor(){}

    constructor(name: String){
        this.name = name
    }

    constructor(name: String, amount: Int){
        this.name = name
        this.amount = amount
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (amount != other.amount) return false
        if (name != other.name) return false
        if (purchased != other.purchased) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + name.hashCode()
        result = 31 * result + purchased.hashCode()
        return result
    }


}