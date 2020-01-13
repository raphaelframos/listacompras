package com.powellapps.compraparamim.ui.newlist

class Product {

    var amount = 1
    var name = ""
    var purchased = false

    constructor(){}

    constructor(name: String){
        this.name = name
    }

    fun map() : HashMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("name", name)
        map.put("amount", amount)
        map.put("purchased", purchased)
        return map
    }


}