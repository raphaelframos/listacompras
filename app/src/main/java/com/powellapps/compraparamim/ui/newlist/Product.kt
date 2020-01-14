package com.powellapps.compraparamim.ui.newlist

class Product {

    var amount = 1
    var name = ""
    var purchased = false

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


}